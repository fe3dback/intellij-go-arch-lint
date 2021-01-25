package com.github.fe3dback.intellijgoarchlint.reference.ref

import com.github.fe3dback.intellijgoarchlint.reference.provider.GoArchSectionReferenceProvider
import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

class GoArchComponentNameReference(element: PsiElement, private val provider: GoArchSectionReferenceProvider):
    PsiReferenceBase<PsiElement>(element),
    EmptyResolveMessageProvider {

    private fun getReferenceTypeName(): String = "GoArch Component Type"
    override fun getUnresolvedMessagePattern(): String {
        return "Incorrect ${getReferenceTypeName()} ''{0}''"
    }

    override fun getVariants() = provider.variants()
    override fun resolve() = provider.match()
}