package com.github.fe3dback.intellijgoarchlint.validate.annotator

import com.github.fe3dback.intellijgoarchlint.exec.linter.InspectionNotice
import com.github.fe3dback.intellijgoarchlint.exec.linter.SDK
import com.github.fe3dback.intellijgoarchlint.exec.linter.contextFromArchFile
import com.github.fe3dback.intellijgoarchlint.project.GoArchFileUtils
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.util.DocumentUtil
import java.nio.file.Path

class LintContext(
    val editor: Editor,
    val virtualFile: VirtualFile,
    val file: Path
)

class LintResult(
    val editor: Editor,
    val lints: List<InspectionNotice>
)

class GoArchSelfInspectionAnnotator : ExternalAnnotator<LintContext, LintResult>() {
    override fun collectInformation(file: PsiFile, editor: Editor, hasErrors: Boolean): LintContext? {
        if (hasErrors) return null
        if (!GoArchFileUtils.isValid(file.virtualFile)) return null

        return LintContext(editor, file.virtualFile, file.virtualFile.toNioPath())
    }

    override fun doAnnotate(collectedInfo: LintContext?): LintResult? {
        val context = collectedInfo ?: return null

        val sdk = SDK(contextFromArchFile(context.virtualFile))
        val result = sdk.selfInspect() ?: return null

        return LintResult(
            context.editor,
            result.Payload.Notices
        )
    }

    override fun apply(file: PsiFile, annotationResult: LintResult?, holder: AnnotationHolder) {
        annotationResult ?: return
        annotationResult.lints.forEach {
            val startOffset = DocumentUtil.calculateOffset(
                annotationResult.editor.document,
                it.Reference.Line - 1,
                0,
                1
            )
            val endOffset = DocumentUtil.calculateOffset(
                annotationResult.editor.document,
                it.Reference.Line - 1,
                it.Reference.Offset - 1,
                1
            )

            holder.newAnnotation(HighlightSeverity.ERROR, it.Text)
                .range(TextRange.create(startOffset, endOffset))
                .create()
        }
    }
}