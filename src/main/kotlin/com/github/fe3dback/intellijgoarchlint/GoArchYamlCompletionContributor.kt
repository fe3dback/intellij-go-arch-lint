package com.github.fe3dback.intellijgoarchlint

import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.psi.GoArchPsiUtils
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

@Deprecated("Prev")
class GoArchYamlCompletionContributor() : CompletionContributor() {
    init {
        extend(
                CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(YAMLLanguage.INSTANCE),
                Provider()
        )
    }

    private class Provider : CompletionProvider<CompletionParameters>() {
        val componentBasedSections = arrayOf(
                GoArch.specDeps,
                GoArch.specCommonComponents
        )

        val vendorBasedSections = arrayOf(
                GoArch.specCommonVendors
        )

        private fun applyCompletion(result: CompletionResultSet, name: String) {
            result.addElement(LookupElementBuilder
                .create(name)
                .withIcon(GoArchIcons.FILETYPE_ICON)
            )
        }

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val psiElement = parameters.position
            if (!GoArchPsiUtils.isGoArchFile(psiElement)) {
                return
            }

            // basic completion
            // -------------------
            val keyValue = PsiTreeUtil.getParentOfType(psiElement, YAMLKeyValue::class.java)
            if (keyValue == null) {
                return
            }

            val topLevelMapping = GoArchPsiUtils.getTopLevelMapping(keyValue) ?: return
            val currentSectionName = keyValue.keyText

            // semantic completion
            // -------------------

            // sem, top - components
            if (componentBasedSections.contains(currentSectionName)) {
                addCompletionComponents(result, topLevelMapping)
            }

            if (vendorBasedSections.contains(currentSectionName)) {
                addCompletionVendors(result, topLevelMapping)
            }

            // sem - deps
            if (currentSectionName == GoArch.specDepsMayDependOn) {
                addCompletionComponents(result, topLevelMapping)
            }

            if (currentSectionName == GoArch.specDepsCanUse) {
                addCompletionVendors(result, topLevelMapping)
            }
        }

        private fun addCompletionComponents(result: CompletionResultSet, topLevel: YAMLMapping) {
            val componentIds = GoArchPsiUtils.getComponentIds(topLevel)

            componentIds.stream().forEachOrdered {
                componentId -> applyCompletion(result, componentId)
            }
        }

        private fun addCompletionVendors(result: CompletionResultSet, topLevel: YAMLMapping) {
            val vendorIds = GoArchPsiUtils.getVendorIds(topLevel)

            vendorIds.stream().forEachOrdered {
                vendorId -> applyCompletion(result, vendorId)
            }
        }
    }
}