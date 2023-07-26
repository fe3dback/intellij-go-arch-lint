package com.github.fe3dback.intellijgoarchlint.config.reference.ref

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.config.reference.provider.GoArchSectionReferenceProvider
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

class GoArchComponentNameReference(
    element: PsiElement,
    range: TextRange,
    private val name: String
) : PsiReferenceBase<PsiElement>(element, range) {
    private val provider = GoArchSectionReferenceProvider(GoArch.specComponents, element)

    override fun getVariants() = provider.variants()
    override fun resolve() = provider.match(name)
}