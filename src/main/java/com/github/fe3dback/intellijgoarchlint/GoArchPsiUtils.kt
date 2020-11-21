package com.github.fe3dback.intellijgoarchlint

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.stream
import org.jetbrains.annotations.Nullable
import org.jetbrains.yaml.YAMLUtil
import org.jetbrains.yaml.psi.*
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl
import java.util.*
import java.util.function.IntFunction
import java.util.function.Predicate
import kotlin.streams.toList

object GoArchPsiUtils {
    private const val topLevelAttributeComponents = "components"
    private const val topLevelAttributeVendors = "vendors"
    private const val topLevelAttributeDependencies = "deps"

    fun getTopLevelMapping(psiElement: PsiElement): YAMLMapping? {
        val doc = PsiTreeUtil.getParentOfType(psiElement, YAMLDocument::class.java) ?: return null
        val topLevelValue = doc.topLevelValue
        if (topLevelValue is YAMLMapping) {
            return topLevelValue
        }

        return null
    }

    private fun getNodeByName(mapping : YAMLMapping, nodeName : String): YAMLKeyValue? {
        val result = mapping.keyValues.stream()
                .filter { keyValue -> keyValue.keyText == nodeName }
                .findFirst()

        if (result.isEmpty) {
            return null
        }

        return result.get()
    }

    private fun getNodeComponents(mapping : YAMLMapping): YAMLKeyValue? {
        return getNodeByName(mapping, topLevelAttributeComponents)
    }

    private fun getNodeVendors(mapping : YAMLMapping): YAMLKeyValue? {
        return getNodeByName(mapping, topLevelAttributeVendors)
    }

    private fun getNodeChildKeys(node: YAMLKeyValue?): List<String> {
        if (node == null) {
            return emptyList()
        }

        val mappingChild = node.children.firstOrNull() ?: return emptyList()
        if (mappingChild !is YAMLBlockMappingImpl) {
            return emptyList()
        }

        return mappingChild.keyValues.stream()
                .map { kv -> kv.keyText }
                .toList()
    }

    fun getComponentIds(mapping : YAMLMapping): List<String> {
        val node = getNodeComponents(mapping) ?: return emptyList()
        return getNodeChildKeys(node)
    }

    fun getVendorIds(mapping : YAMLMapping): List<String> {
        val node = getNodeVendors(mapping) ?: return emptyList()
        return getNodeChildKeys(node)
    }

    fun isGoArchFile(element: PsiElement): Boolean {
        val file = element.containingFile

        if (file !is YAMLFile) {
            return false
        }

        val keys = YAMLUtil.getTopLevelKeys(file)

        return keys.stream()
                .map { obj: YAMLKeyValue -> obj.keyText }
                .anyMatch { anObject: String -> topLevelAttributeComponents == anObject }
            && keys.stream()
                .map { obj: YAMLKeyValue -> obj.keyText }
                .anyMatch { anObject: String -> topLevelAttributeDependencies == anObject }
    }
}