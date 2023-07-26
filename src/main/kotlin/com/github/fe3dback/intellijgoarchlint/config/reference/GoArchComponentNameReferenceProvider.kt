package com.github.fe3dback.intellijgoarchlint.config.reference

import com.github.fe3dback.intellijgoarchlint.config.reference.ref.GoArchComponentNameReference
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLScalar

class GoArchComponentNameReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element is YAMLKeyValue) {
            return arrayOf(attachToKeyValue(element))
        }

        if (element is YAMLScalar) {
            return arrayOf(attachToScalar(element))
        }

        return PsiReference.EMPTY_ARRAY
    }

    private fun attachToScalar(element: YAMLScalar): PsiReference {
        val constRange = TextRange(0, element.textValue.length)
        return GoArchComponentNameReference(element, constRange, element.textValue)
    }

    private fun attachToKeyValue(element: YAMLKeyValue): PsiReference {
        val key = element.key!!
        val constRange = TextRange(0, key.text.length)
        return GoArchComponentNameReference(element, constRange, key.text)
    }
}

