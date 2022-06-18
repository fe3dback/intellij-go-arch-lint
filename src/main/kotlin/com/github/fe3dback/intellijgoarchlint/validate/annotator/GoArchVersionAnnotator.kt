package com.github.fe3dback.intellijgoarchlint.validate.annotator

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.file.GoArchFileType
import com.github.fe3dback.intellijgoarchlint.reference.pattern.GoArchPsiPattern
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLKeyValue

class GoArchVersionAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val virtualFile = element.containingFile.virtualFile ?: return

        if (!GoArchFileType.INSTANCE.isMyFileType(virtualFile)) {
            // yaml element, not inside goArch config
            return
        }

        if (!GoArchPsiPattern.version().accepts(element)) {
            // is not version element
            return
        }

        val version = documentVersion(element)
        if (version == GoArch.versionMaximum) {
            // using the latest version, it's good
            return
        }

        if (version > 0 && version < GoArch.versionMaximum) {
            holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "New version is available: ${GoArch.versionMaximum}")
                .create()
            return
        }

        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Invalid version: $version, available [%d .. %d]".format(
                GoArch.versionMinimum,
                GoArch.versionMaximum,
            )
        ).create()
    }

    private fun documentVersion(element: PsiElement): Int {
        if (element !is YAMLKeyValue) {
            return 0
        }

        return try {
            element.valueText.format("%d").toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }
}