package com.github.fe3dback.intellijgoarchlint.config.reference.ref

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.config.reference.provider.GoArchSectionReferenceProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

class GoArchVendorNameReference(
    element: PsiElement,
    private val name: String
) : PsiReferenceBase<PsiElement>(element) {
    private val provider = GoArchSectionReferenceProvider(GoArch.specVendors, element)

    override fun getVariants() = provider.variants()
    override fun resolve() = provider.match(name)
}