package com.github.fe3dback.intellijgoarchlint.config.annotations

import com.github.fe3dback.intellijgoarchlint.models.Annotation
import com.intellij.openapi.editor.Editor

class LintResult(
    val editor: Editor,
    val lints: List<Annotation>
)

