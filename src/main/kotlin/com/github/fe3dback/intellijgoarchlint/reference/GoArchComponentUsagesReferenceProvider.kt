package com.github.fe3dback.intellijgoarchlint.reference

import com.github.fe3dback.intellijgoarchlint.reference.ref.GoArchComponentUsagesReference
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLKeyValue

class GoArchComponentUsagesReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is YAMLKeyValue) return PsiReference.EMPTY_ARRAY

        return arrayOf(GoArchComponentUsagesReference(element, element.keyText))
    }
}

