package com.github.fe3dback.intellijgoarchlint.reference.ref

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.psi.GoArchPsiUtils
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.*
import com.intellij.util.containers.stream
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLPsiElement
import java.util.stream.Stream

class GoArchComponentUsagesReference(
    element: PsiElement,
    private val name: String
): PsiReferenceBase<PsiElement>(element), PsiPolyVariantReference {
    override fun getVariants(): Array<Any> {
        val itemsStream = stream() ?: return emptyArray()

        return itemsStream
            .map {
                LookupElementBuilder.create(it)
                    .withIcon(GoArchIcons.FILETYPE_ICON)
            }
            .toArray()
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return elementName(element) == name // todo: not sure its needed
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val itemsStream = streamByName(name, incompleteCode) ?: return emptyArray()
        var result = emptyArray<ResolveResult>()
        itemsStream.forEach {
            result = result.plus(PsiElementResolveResult(it))
        }

        return result
    }

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        if (results.count() == 1) {
            return results.first().element
        }

        return null
    }

    private fun streamByName(name: String, incompleteCode: Boolean): Stream<YAMLPsiElement>? {
        val stream = stream() ?: return null

        if (incompleteCode) {
            return stream.filter { elementName(it).contains(name, true) }
        }

        return stream.filter { elementName(it) == name }
    }

    private fun elementName(element: PsiElement): String {
        if (element is YAMLKeyValue) {
            return element.keyText
        }

        return element.text
    }

    private fun stream(): Stream<YAMLPsiElement>? {
        val mapping = GoArchPsiUtils.getTopLevelMapping(element) ?: return null
        val nodeCommonComponents = GoArchPsiUtils.getNodeByName(mapping, GoArch.specCommonComponents)
        val nodeDeps = GoArchPsiUtils.getNodeByName(mapping, GoArch.specDeps)

        var result = emptyArray<YAMLPsiElement>()

        if (nodeCommonComponents != null) {
            GoArchPsiUtils.getNodeSequenceItemsStream(nodeCommonComponents)
                .forEach { result = result.plus(it) }
        }

        if (nodeDeps != null) {
            GoArchPsiUtils.getNodeKeyValuesStream(nodeDeps)
                .filter { it.key != null }
                .forEach { result = result.plus(it) }
        }

        return result.stream()
            .distinct()
    }
}