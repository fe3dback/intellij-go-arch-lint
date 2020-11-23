package com.github.fe3dback.intellijgoarchlint

import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import java.util.*

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

        val topLevelKeyWords = arrayOf(
                GoArch.specVersion,
                GoArch.specAllow,
                GoArch.specExclude,
                GoArch.specExcludeFiles,
                GoArch.specComponents,
                GoArch.specVendors,
                GoArch.specCommonComponents,
                GoArch.specCommonVendors,
                GoArch.specDeps
        )

        val depsKeyWords = arrayOf(
                GoArch.specDepsCanUse,
                GoArch.specDepsMayDependOn,
                GoArch.specDepsAnyVendorDeps,
                GoArch.specDepsAnyProjectDeps,
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
                addCompletionTopLevel(result)
                return
            }

            val topLevelMapping = GoArchPsiUtils.getTopLevelMapping(keyValue) ?: return
            val currentSectionName = keyValue.keyText

            // keyWords deps completion
            // -------------------
            val grandParent = keyValue.parentOfType<YAMLKeyValue>()
            if (grandParent != null) {
                val grandParentIsTopSection = grandParent.parentOfType<YAMLKeyValue>() == null
                if (grandParentIsTopSection && grandParent.keyText == GoArch.specDeps) {
                    addDepsKeyWordCompletion(result)
                }
            }

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

        private fun addCompletionTopLevel(result: CompletionResultSet) {
            Arrays.stream(topLevelKeyWords).forEachOrdered {
                kw: String -> applyCompletion(result, kw)
            }
        }

        private fun addDepsKeyWordCompletion(result: CompletionResultSet) {
            Arrays.stream(depsKeyWords).forEachOrdered {
                kw: String -> applyCompletion(result, kw)
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