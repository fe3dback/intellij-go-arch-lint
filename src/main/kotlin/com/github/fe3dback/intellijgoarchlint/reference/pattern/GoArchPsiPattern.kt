package com.github.fe3dback.intellijgoarchlint.reference.pattern

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.*

object GoArchPsiPattern {
//    fun componentNames() = sectionKeys(GoArch.specComponents)
//    fun vendorNames() = sectionKeys(GoArch.specVendors)
    fun commonComponents() = sectionSequenceItems(GoArch.specCommonComponents)
    fun commonVendors() = sectionSequenceItems(GoArch.specCommonVendors)

//    private fun sectionKeys(sectionName: String): PsiElementPattern.Capture<PsiElement> {
//        return PlatformPatterns.psiElement()
//            .withParent(
//                abstractKeyValue()
//                    .withParent(
//                        abstractMapping()
//                            .withParent(
//                                section(sectionName)
//                            )
//                    )
//            )
//    }

    private fun sectionSequenceItems(sectionName: String): PsiElementPattern.Capture<PsiElement> {
        return PlatformPatterns.psiElement()
            .inside(
                section(sectionName)
            )
    }

    private fun section(sectionName: String): PsiElementPattern.Capture<YAMLKeyValue> {
        return keyValue(sectionName)
            .inside(
                topLevelMapping()
            )
    }

    private fun keyValue(name: String): PsiElementPattern.Capture<YAMLKeyValue> {
        return abstractKeyValue()
            .withName(
                PlatformPatterns.string().equalTo(name)
            )
    }

    private fun abstractKeyValue(): PsiElementPattern.Capture<YAMLKeyValue> {
        return PlatformPatterns.psiElement(YAMLKeyValue::class.java)
    }

    private fun abstractMapping(): PsiElementPattern.Capture<YAMLMapping> {
        return PlatformPatterns.psiElement(YAMLMapping::class.java)
    }

    private fun topLevelMapping(): PsiElementPattern.Capture<YAMLMapping> =
        abstractMapping()
            .inside(
                yamlDocument()
            )

    private fun yamlDocument(): PsiElementPattern.Capture<YAMLDocument> =
        PlatformPatterns.psiElement(YAMLDocument::class.java)
}