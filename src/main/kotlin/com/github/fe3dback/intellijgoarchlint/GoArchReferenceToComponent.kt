package com.github.fe3dback.intellijgoarchlint

import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.*
import org.jetbrains.yaml.psi.YAMLKeyValue

class GoArchReferenceToComponent(psiElement: YAMLKeyValue) : PsiReferenceBase<YAMLKeyValue>(psiElement), PsiPolyVariantReference {
    private val nodeComponents = "components"

    override fun resolve(): PsiElement? {
        val result = multiResolve(false).firstOrNull() ?: return null
        return result.element
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val name = element.keyText
        if (name == "") {
            return emptyArray()
        }

        val topMapping = GoArchPsiUtils.getTopLevelMapping(element) ?: return emptyArray()
        val componentsNode = GoArchPsiUtils.getNodeByName(topMapping, nodeComponents) ?: return emptyArray()
        val component = GoArchPsiUtils.getFirstKeyValueInNodeByName(componentsNode, name) ?: return emptyArray()
        val key = component.key ?: return emptyArray()

        return arrayOf(PsiElementResolveResult(key))
    }

    override fun getVariants(): Array<LookupElement> {
        val topMapping = GoArchPsiUtils.getTopLevelMapping(element) ?: return emptyArray()
        val componentsNode = GoArchPsiUtils.getNodeByName(topMapping, nodeComponents)
        val results : ArrayList<LookupElement> = ArrayList()

        GoArchPsiUtils.getNodeKeyValuesStream(componentsNode).forEach {
            results.add(LookupElementBuilder
                .create(it.keyText)
                .withIcon(GoArchIcons.FILETYPE_ICON)
                .withTypeText(nodeComponents)
            )
        }

        return results.toTypedArray()
    }
}