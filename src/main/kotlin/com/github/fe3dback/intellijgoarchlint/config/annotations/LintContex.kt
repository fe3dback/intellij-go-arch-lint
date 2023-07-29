package com.github.fe3dback.intellijgoarchlint.config.annotations

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class LintContext(
    val editor: Editor,
    val configPsiFile: PsiFile,
)
