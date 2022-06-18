package com.github.fe3dback.intellijgoarchlint.exec.linter

import com.intellij.openapi.vfs.VirtualFile

data class Context(
    val projectPath: String = "",
    val goArchFileName: String = ".go-arch-lint.yml",
)

fun contextFromArchFile(file: VirtualFile): Context {
    val directory = file.parent.path
    val configName = file.name

    return Context(directory, configName)
}