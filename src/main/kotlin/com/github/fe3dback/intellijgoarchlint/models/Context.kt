package com.github.fe3dback.intellijgoarchlint.models

import com.intellij.openapi.project.Project

data class Context(
    val project: Project,
    val projectPath: String,
    val configName: String,
)