package com.github.fe3dback.intellijgoarchlint.schema

import com.intellij.openapi.project.Project
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory

class GoArchJsonSchemaProviderFactory : JsonSchemaProviderFactory {
    override fun getProviders(project: Project) =
        listOf(
            GoArchJsonSchemaProvider(project, 3, false),
            GoArchJsonSchemaProvider(project, 2, false),
            GoArchJsonSchemaProvider(project, 1, false),
            GoArchJsonSchemaProvider(project, 0, true),
        )
}