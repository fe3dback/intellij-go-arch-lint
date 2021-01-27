package com.github.fe3dback.intellijgoarchlint.reference

import com.github.fe3dback.intellijgoarchlint.reference.ref.GoArchComponentNameReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class GoArchComponentNameReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is YAMLPlainTextImpl) return PsiReference.EMPTY_ARRAY

        val constRange = TextRange(0, element.textValue.length)

        return arrayOf(GoArchComponentNameReference(element, constRange, element.textValue))
    }
}

