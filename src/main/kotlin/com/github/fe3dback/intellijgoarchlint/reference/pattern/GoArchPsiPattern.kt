package com.github.fe3dback.intellijgoarchlint.reference.pattern

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.*

object GoArchPsiPattern {
    fun insideKeyValue(name: String): PsiElementPattern.Capture<PsiElement> {
        return PlatformPatterns.psiElement()
            .inside(
                PlatformPatterns
                    .psiElement(YAMLKeyValue::class.java)
                    .withName(
                        PlatformPatterns.string().equalTo(name)
                    )
            )
    }
}