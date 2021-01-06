package com.github.fe3dback.intellijgoarchlint.reference

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.psi.GoArchPsiUtils
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLScalarText
import kotlin.streams.toList

class GoArchComponentNameReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is YAMLScalarText) return emptyArray()
        return arrayOf(GoArchComponentNameReference(element))
    }
}

private class GoArchComponentNameReference(element: YAMLScalarText): PsiPolyVariantReferenceBase<YAMLScalarText>(element) {
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val mapping = GoArchPsiUtils.getTopLevelMapping(element) ?: return emptyArray()
        val componentNode = GoArchPsiUtils.getNodeByName(mapping, GoArch.specComponents) ?: return emptyArray()
        val componentsStream = GoArchPsiUtils.getNodeKeyValuesStream(componentNode)

        return componentsStream
            .map { PsiElementResolveResult(it) }
            .toList()
            .toTypedArray()
    }
}