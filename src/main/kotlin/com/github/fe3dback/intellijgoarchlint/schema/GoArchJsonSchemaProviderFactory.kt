package com.github.fe3dback.intellijgoarchlint.schema

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.intellij.openapi.project.Project
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory

class GoArchJsonSchemaProviderFactory : JsonSchemaProviderFactory {
    override fun getProviders(project: Project): MutableList<JsonSchemaFileProvider> {
        val list = mutableListOf<JsonSchemaFileProvider>(
            GoArchJsonSchemaProvider(project, 0, true),
        )

        for (version in GoArch.versionMinimum .. GoArch.versionMaximum) {
            list.add(GoArchJsonSchemaProvider(project, version, false))
        }

        return list
    }
}