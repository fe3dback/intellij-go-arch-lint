package com.github.fe3dback.intellijgoarchlint.reference.provider

import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.psi.GoArchPsiUtils
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import org.jetbrains.yaml.psi.YAMLKeyValue
import java.util.stream.Stream

class GoArchSectionReferenceProvider(
    private val sectionName: String,
    private val sourceElement: PsiElement
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

    fun match(name: String): PsiElement? {
        val itemsStream = stream() ?: return null

        val firstMatch = itemsStream
            .filter { it.key!!.text == name }
            .map {
                PsiElementResolveResult(it.firstChild)
//                GoArchSectionPsiElement(
//                    it.key!!,
//                    sectionName,
//                    element.textValue
//                )
            }
            .findFirst()

        if (firstMatch.isEmpty) {
            return null
        }

        return firstMatch.get().element
    }

    private fun stream(): Stream<YAMLKeyValue>? {
        val mapping = GoArchPsiUtils.getTopLevelMapping(sourceElement) ?: return null
        val componentNode = GoArchPsiUtils.getNodeByName(mapping, sectionName) ?: return null
        val componentsStream = GoArchPsiUtils.getNodeKeyValuesStream(componentNode)

        return componentsStream.filter { it.key != null }
    }
}