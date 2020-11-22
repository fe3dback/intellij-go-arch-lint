package com.github.fe3dback.intellijgoarchlint

import com.intellij.lang.HelpID
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.jetbrains.yaml.psi.YAMLKeyValue

class GoArchYamlFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner {
        return GoArchYamlWordScanner()
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is PsiNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return HelpID.FIND_OTHER_USAGES
    }

    override fun getType(element: PsiElement): String {
        if (element is YAMLKeyValue) {
            return "property"
        }

        return ""
    }

    override fun getDescriptiveName(element: PsiElement): String {
        if (element is PsiNamedElement) {
            val name = element.name
            if (name != null) {
                return name
            }
        }

        return "<unnamed>"
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return getDescriptiveName(element)
    }
}