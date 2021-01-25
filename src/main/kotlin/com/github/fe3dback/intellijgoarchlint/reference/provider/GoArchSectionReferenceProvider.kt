package com.github.fe3dback.intellijgoarchlint.reference.provider

import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.psi.GoArchPsiUtils
import com.github.fe3dback.intellijgoarchlint.reference.element.GoArchSectionPsiElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl
import java.util.stream.Stream

class GoArchSectionReferenceProvider(
    private val element: YAMLPlainTextImpl,
    private val sectionName: String
) {
    fun variants(): Array<Any>  {
        val itemsStream = stream() ?: return emptyArray()

        return itemsStream
            .map {
                LookupElementBuilder.create(it)
                    .withIcon(GoArchIcons.FILETYPE_ICON)
                    .withTypeText(sectionName)
            }
            .toArray()
    }

    fun match(): GoArchSectionPsiElement? {
        val itemsStream = stream() ?: return null

        val firstMatch = itemsStream
            .filter { it.key!!.text == element.textValue }
            .map {
                GoArchSectionPsiElement(
                    it.key!!,
                    sectionName,
                    element.textValue
                )
            }
            .findFirst()

        if (firstMatch.isEmpty) {
            return null
        }

        return firstMatch.get()
    }

    private fun stream(): Stream<YAMLKeyValue>? {
        val mapping = GoArchPsiUtils.getTopLevelMapping(element) ?: return null
        val componentNode = GoArchPsiUtils.getNodeByName(mapping, sectionName) ?: return null
        val componentsStream = GoArchPsiUtils.getNodeKeyValuesStream(componentNode)

        return componentsStream.filter { it.key != null }
    }
}