package com.github.fe3dback.intellijgoarchlint.config.reference.pattern

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.*

object GoArchPsiPattern {
    fun version() = rootKeyValue(GoArch.specVersion)
    fun excludeFiles() = sectionSequenceItems(GoArch.specExcludeFiles)
    fun components() = sectionKeys(GoArch.specComponents)
    fun vendors() = sectionKeys(GoArch.specVendors)
    fun componentDependencies() = sectionKeys(GoArch.specDeps)
    fun commonComponents() = sectionSequenceItems(GoArch.specCommonComponents)
    fun commonVendors() = sectionSequenceItems(GoArch.specCommonVendors)
    fun mayDependInDependencies() = depsSectionItems(GoArch.specDepsMayDependOn)
    fun canUseInDependencies() = depsSectionItems(GoArch.specDepsCanUse)

    private fun depsSectionItems(attributeName: String): PsiElementPattern.Capture<PsiElement> {
        return sequenceItemsInsideKeyValue(
            keyValue(attributeName)
                .inside(
                    sectionKeys(GoArch.specDeps)
                )
        )
    }

    private fun sectionKeys(sectionName: String): PsiElementPattern.Capture<YAMLKeyValue> {
        return abstractKeyValue()
            .withSuperParent(2, rootKeyValue(sectionName))
    }

    private fun sectionSequenceItems(sectionName: String): PsiElementPattern.Capture<PsiElement> {
        return sequenceItemsInsideKeyValue(rootKeyValue(sectionName))
    }

    private fun sequenceItemsInsideKeyValue(keyValue: PsiElementPattern.Capture<YAMLKeyValue>): PsiElementPattern.Capture<PsiElement> {
        return PlatformPatterns.psiElement()
            .inside(
                sequenceItemOfKeyValue(keyValue)
            )
    }

    private fun sequenceItemOfKeyValue(keyValue: PsiElementPattern.Capture<YAMLKeyValue>): PsiElementPattern.Capture<YAMLSequenceItem> {
        return PlatformPatterns.psiElement(YAMLSequenceItem::class.java)
            .withParent(
                PlatformPatterns.psiElement(YAMLSequence::class.java)
                    .withParent(keyValue)
            )
    }

    private fun rootKeyValue(sectionName: String): PsiElementPattern.Capture<YAMLKeyValue> {
        return keyValue(sectionName)
            .withParent(
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

    private fun topLevelMapping(): PsiElementPattern.Capture<YAMLMapping> =
        PlatformPatterns.psiElement(YAMLMapping::class.java)
            .withParent(
                PlatformPatterns.psiElement(YAMLDocument::class.java)
            )
}