package com.github.fe3dback.intellijgoarchlint.reference

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.psi.GoArchPsiUtils
import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.*
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceUtil
import com.intellij.psi.meta.PsiMetaData
import com.intellij.psi.meta.PsiMetaOwner
import com.intellij.psi.meta.PsiPresentableMetaData
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar
import org.jetbrains.yaml.psi.YAMLScalarText
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl
import org.jetbrains.yaml.resolve.YAMLAliasReference
import java.util.stream.Stream
import javax.swing.Icon

class GoArchComponentNameReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is YAMLPlainTextImpl) return PsiReference.EMPTY_ARRAY

        return arrayOf(GoArchComponentNameReference(element))
    }
}

private class GoArchComponentNameReference(element: PsiElement):
    PsiReferenceBase<PsiElement>(element),
    EmptyResolveMessageProvider {

    private fun getReferenceTypeName(): String = "GoArch Component Type"

    override fun resolve(): PsiElement? {
        if (element !is YAMLPlainTextImpl) {
            return null
        }

        val stream = componentsStream() ?: return null
        val firstMatch = stream
            .filter { it.key!!.text == (element as YAMLPlainTextImpl).textValue }
            .map { PsiElementResolveResult(it.key!!) }
            .findFirst()

        if (firstMatch.isEmpty) {
            return null
        }

        return firstMatch.get().element
    }

    override fun getVariants(): Array<Any> {
        val stream = componentsStream() ?: return emptyArray()

        return stream
            .map {
                LookupElementBuilder.create(it)
                    .withIcon(GoArchIcons.FILETYPE_ICON)
            }
            .toArray()
    }

    override fun getUnresolvedMessagePattern(): String {
        return "Incorrect ${getReferenceTypeName()} ''{0}''"
    }

    private fun componentsStream(): Stream<YAMLKeyValue>? {
        val mapping = GoArchPsiUtils.getTopLevelMapping(element) ?: return null
        val componentNode = GoArchPsiUtils.getNodeByName(mapping, GoArch.specComponents) ?: return null
        val componentsStream = GoArchPsiUtils.getNodeKeyValuesStream(componentNode)

        return componentsStream.filter { it.key != null }
    }
}
