package com.github.fe3dback.intellijgoarchlint.config.annotations

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.vfs.VirtualFile

class LintContext(
    val editor: Editor,
    val virtualFile: VirtualFile,
)
