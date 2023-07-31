package com.github.fe3dback.intellijgoarchlint.config.reference

import com.github.fe3dback.intellijgoarchlint.config.reference.ref.GoArchVendorNameReference
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class GoArchVendorNameReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is YAMLPlainTextImpl) return PsiReference.EMPTY_ARRAY

        return arrayOf(GoArchVendorNameReference(element, element.textValue))
    }
}

