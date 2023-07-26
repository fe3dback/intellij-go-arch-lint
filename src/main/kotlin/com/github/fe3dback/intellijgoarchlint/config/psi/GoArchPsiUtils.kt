package com.github.fe3dback.intellijgoarchlint.config.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl
import java.util.stream.Stream

object GoArchPsiUtils {
    fun getTopLevelMapping(psiElement: PsiElement): YAMLMapping? {
        val doc = PsiTreeUtil.getParentOfType(psiElement, YAMLDocument::class.java) ?: return null
        val topLevelValue = doc.topLevelValue
        if (topLevelValue !is YAMLMapping) {
            return null
        }

        return topLevelValue
    }

    fun getNodeByName(mapping: YAMLMapping, nodeName: String): YAMLKeyValue? {
        val result = mapping.keyValues.stream()
            .filter { keyValue -> keyValue.keyText == nodeName }
            .findFirst()

        if (result.isEmpty) {
            return null
        }

        return result.get()
    }

    fun getNodeKeyValuesStream(node: YAMLKeyValue?): Stream<YAMLKeyValue> {
        if (node == null) {
            return Stream.empty()
        }

        val mappingChild = node.children.firstOrNull() ?: return Stream.empty()
        if (mappingChild !is YAMLBlockMappingImpl) {
            return Stream.empty()
        }

        return mappingChild.keyValues.stream()
    }

}