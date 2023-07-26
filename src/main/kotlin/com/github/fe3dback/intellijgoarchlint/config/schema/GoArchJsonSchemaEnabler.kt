package com.github.fe3dback.intellijgoarchlint.config.schema

import com.github.fe3dback.intellijgoarchlint.config.file.GoArchFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaEnabler

class GoArchJsonSchemaEnabler : JsonSchemaEnabler {
    override fun isEnabledForFile(file: VirtualFile, project: Project?): Boolean {
        return GoArchFileType.INSTANCE.isMyFileType(file)
    }
}