package com.github.fe3dback.intellijgoarchlint.reference

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.psi.GoArchPsiUtils
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLScalarText

class GoArchComponentNameReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is YAMLScalarText) return PsiReference.EMPTY_ARRAY
        return arrayOf(GoArchComponentNameReference(element))
    }
}

private class GoArchComponentNameReference(element: YAMLScalarText): PsiReferenceBase<YAMLScalarText>(element) {
    override fun resolve(): PsiElement? {
        val mapping = GoArchPsiUtils.getTopLevelMapping(element) ?: return null
        val componentNode = GoArchPsiUtils.getNodeByName(mapping, GoArch.specComponents) ?: return null
        val componentsStream = GoArchPsiUtils.getNodeKeyValuesStream(componentNode)

        val firstMatch = componentsStream
            .filter { it.key != null }
            .filter { it.key!!.text == element.textValue }
            .map { PsiElementResolveResult(it.key!!) }
            .findFirst()

        if (firstMatch.isEmpty) {
            return null
        }

        return firstMatch.get().element
    }
}