package com.github.fe3dback.intellijgoarchlint.config.annotations

import com.github.fe3dback.intellijgoarchlint.cmp.integration.sdk.SDKProvider
import com.github.fe3dback.intellijgoarchlint.models.Annotation
import com.github.fe3dback.intellijgoarchlint.models.Context
import com.github.fe3dback.intellijgoarchlint.project.GoArchFileUtils
import com.github.fe3dback.intellijgoarchlint.settings.Features
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
        if (!Features.isConfigLintEnabled(file.project)) return null

        return LintContext(editor, file)
    }

    override fun doAnnotate(collectedInfo: LintContext?): LintResult? {
        if (collectedInfo == null) {
            return null
        }

        val sdk = SDKProvider.goArchLintSDK()
        return LintResult(
            collectedInfo.editor, sdk.configIssues(
                Context(
                    collectedInfo.configPsiFile.project,
                    collectedInfo.configPsiFile.virtualFile.parent.path,
                    collectedInfo.configPsiFile.virtualFile.name,
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