package com.github.fe3dback.intellijgoarchlint.config.annotations

import com.github.fe3dback.intellijgoarchlint.integration.sdk.Provider
import com.github.fe3dback.intellijgoarchlint.models.Annotation
import com.github.fe3dback.intellijgoarchlint.models.Context
import com.github.fe3dback.intellijgoarchlint.project.GoArchFileUtils
import com.github.fe3dback.intellijgoarchlint.settings.goArchLintStorage
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.util.DocumentUtil

class ExternalAnnotator : ExternalAnnotator<LintContext, LintResult>() {
    override fun collectInformation(file: PsiFile, editor: Editor, hasErrors: Boolean): LintContext? {
        if (hasErrors) return null
        if (!GoArchFileUtils.isValid(file.virtualFile)) return null
        if (!file.project.goArchLintStorage.state.enableIntegrations) return null
        if (!file.project.goArchLintStorage.state.enableSubSelfInspections) return null

        return LintContext(editor, file.virtualFile)
    }

    override fun doAnnotate(collectedInfo: LintContext?): LintResult? {
        if (collectedInfo == null) {
            return null
        }


        val sdk = Provider().archSDK
        return LintResult(
            collectedInfo.editor, sdk.configIssues(
                Context(
                    collectedInfo.virtualFile.parent.path,
                    collectedInfo.virtualFile.name,
                )
            )
        )
    }

    override fun apply(
        file: PsiFile,
        annotationResult: LintResult?,
        holder: AnnotationHolder
    ) {
        annotationResult ?: return
        annotationResult.lints.forEach {
            applyAnnotation(it, annotationResult, holder)
        }
    }

    private fun applyAnnotation(
        it: Annotation,
        annotationResult: LintResult,
        holder: AnnotationHolder
    ) {
        if (!it.reference.valid) return

        val startOffset = DocumentUtil.calculateOffset(
            annotationResult.editor.document,
            it.reference.line - 1,
            it.reference.offset - 1,
            1
        )
        val endOffset = DocumentUtil.calculateOffset(
            annotationResult.editor.document,
            it.reference.line - 1,
            it.reference.offset + 100,
            1
        )

        holder.newAnnotation(HighlightSeverity.ERROR, it.message)
            .range(TextRange.create(startOffset, endOffset))
            .create()
    }
}