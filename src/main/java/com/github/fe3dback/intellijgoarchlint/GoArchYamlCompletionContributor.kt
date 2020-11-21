package com.github.fe3dback.intellijgoarchlint

import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.intellij.util.containers.stream
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
        private val keyWordDependencies = "deps"
        private val keyWordVersion = "version"
        private val keyWordAllow = "allow"
        private val keyWordExclude = "exclude"
        private val keyWordExcludeFiles = "excludeFiles"
        private val keyWordComponents = "components"
        private val keyWordVendors = "vendors"
        private val keyWordCommonComponents = "commonComponents"
        private val keyWordCommonVendors = "commonVendors"

        private val keyWordProviderComponents = "mayDependOn"
        private val keyWordProviderVendors = "canUse"

        private val keyWordsTopLevel = arrayOf(
                keyWordVersion,
                keyWordAllow,
                keyWordExclude,
                keyWordExcludeFiles,
                keyWordComponents,
                keyWordVendors,
                keyWordCommonComponents,
                keyWordCommonVendors,
                keyWordDependencies
        )

        private val componentBasedSections = arrayOf(
                keyWordDependencies,
                keyWordCommonComponents
        )

        private val vendorBasedSections = arrayOf(
                keyWordCommonVendors
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

            // semantic completion
            // -------------------
            val topLevelMapping = GoArchPsiUtils.getTopLevelMapping(keyValue) ?: return
            val currentSectionName = keyValue.keyText

            // sem, top - components
            if (componentBasedSections.contains(currentSectionName)) {
                addCompletionComponents(result, topLevelMapping)
            }

            if (vendorBasedSections.contains(currentSectionName)) {
                addCompletionVendors(result, topLevelMapping)
            }

            // sem - deps
            if (currentSectionName == keyWordProviderComponents) {
                addCompletionComponents(result, topLevelMapping)
            }

            if (currentSectionName == keyWordProviderVendors) {
                addCompletionVendors(result, topLevelMapping)
            }
        }

        private fun addCompletionTopLevel(result: CompletionResultSet) {
            Arrays.stream(keyWordsTopLevel).forEachOrdered {
                kw: String -> applyCompletion(result, kw)
            }
        }

        private fun addCompletionComponents(result: CompletionResultSet, topLevel: YAMLMapping) {
            val componentIds = GoArchPsiUtils.getComponentIds(topLevel)

            // todo: inner places: [deps.$.mayDependOn]
            componentIds.stream().forEachOrdered {
                componentId -> applyCompletion(result, componentId)
            }
        }

        private fun addCompletionVendors(result: CompletionResultSet, topLevel: YAMLMapping) {
            val vendorIds = GoArchPsiUtils.getVendorIds(topLevel)

            // todo: inner places: [deps.$.canUse]
            vendorIds.stream().forEachOrdered {
                vendorId -> applyCompletion(result, vendorId)
            }
        }
    }
}