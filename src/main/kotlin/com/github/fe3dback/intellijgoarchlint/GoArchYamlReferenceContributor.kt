package com.github.fe3dback.intellijgoarchlint

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.*
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.YAMLLanguage
import org.jetbrains.yaml.psi.YAMLKeyValue

class GoArchYamlReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            yamlKeyValuePattern(),
            DepsReferenceProvider()
        )
    }

    private fun yamlKeyValuePattern() : PsiElementPattern.Capture<YAMLKeyValue> {
        return PlatformPatterns.psiElement(YAMLKeyValue::class.java)
            .withLanguage(YAMLLanguage.INSTANCE)
    }

    private class DepsReferenceProvider : PsiReferenceProvider() {
        override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
            if (!GoArchPsiUtils.isGoArchFile(element)) {
                return emptyArray()
            }

            // find parent of current element
            val yamlKeyValue = element as YAMLKeyValue
            val parent = yamlKeyValue.parentOfType<YAMLKeyValue>() ?: return emptyArray()

            // check than current section has deps name
            if (parent.keyText != GoArch.specDeps) {
                return emptyArray()
            }

            // if this is top level section (parent not have another parents)
            if (parent.parentOfType<YAMLKeyValue>() != null) {
                return emptyArray()
            }

            // ok, this is deps section, and componentName, we can link it to components.%name%
            return arrayOf(GoArchReferenceToSection(yamlKeyValue, GoArch.specComponents))
        }
    }
}