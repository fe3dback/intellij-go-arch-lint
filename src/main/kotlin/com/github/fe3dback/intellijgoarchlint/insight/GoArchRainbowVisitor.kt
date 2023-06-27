package com.github.fe3dback.intellijgoarchlint.insight

import com.github.fe3dback.intellijgoarchlint.file.GoArchFileType
import com.github.fe3dback.intellijgoarchlint.reference.pattern.GoArchPsiPattern
import com.intellij.codeInsight.daemon.RainbowVisitor
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl

class GoArchRainbowVisitor : RainbowVisitor() {
    override fun clone(): HighlightVisitor = GoArchRainbowVisitor()
    override fun suitableForFile(file: PsiFile): Boolean {
        return GoArchFileType.INSTANCE.isMyFileType(file.virtualFile)
    }

    override fun visit(element: PsiElement) {
        if (!isComponent(element)) {
            return
        }

        when (element) {
            is YAMLKeyValue -> applyToYamlKey(element)
            is YAMLPlainTextImpl -> applyToSequenceItem(element)
        }
    }

    private fun applyToYamlKey(element: YAMLKeyValue) {
        highlight(element, element.key!!, element.keyText)
    }

    private fun applyToSequenceItem(element: YAMLPlainTextImpl) {
        highlight(element.parent, element, element.text)
    }

    private fun highlight(context: PsiElement, element: PsiElement, id: String) {
        addInfo(getInfo(context, element, id, TextAttributesKey.createTextAttributesKey(id)))
    }

    private fun isPossibleComponent(element: PsiElement): Boolean {
//        if (element is YAMLKeyValue) {
//            return true
//        }

        if (element is YAMLPlainTextImpl) {
            return true
        }

        return false
    }

    private fun isComponent(element: PsiElement): Boolean {
        if (!isPossibleComponent(element)) {
            return false
        }

        if (GoArchPsiPattern.commonComponents().accepts(element)) {
            return true
        }

        if (GoArchPsiPattern.commonVendors().accepts(element)) {
            return true
        }

//        if (GoArchPsiPattern.components().accepts(element)) {
//            return true
//        }
//
//        if (GoArchPsiPattern.vendors().accepts(element)) {
//            return true
//        }
//
//        if (GoArchPsiPattern.componentDependencies().accepts(element)) {
//            return true
//        }

        if (GoArchPsiPattern.mayDependInDependencies().accepts(element)) {
            return true
        }

        if (GoArchPsiPattern.canUseInDependencies().accepts(element)) {
            return true
        }

        return false
    }
}