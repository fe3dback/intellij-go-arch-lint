package com.github.fe3dback.intellijgoarchlint.reference

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.reference.provider.GoArchSectionReferenceProvider
import com.github.fe3dback.intellijgoarchlint.reference.ref.GoArchVendorNameReference
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class GoArchVendorNameReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is YAMLPlainTextImpl) return PsiReference.EMPTY_ARRAY

        val sectionProvider = GoArchSectionReferenceProvider(element, GoArch.specVendors)

        return arrayOf(GoArchVendorNameReference(element, sectionProvider))
    }
}

