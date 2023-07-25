package com.github.fe3dback.intellijgoarchlint.schema

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.file.GoArchFileType
import com.github.fe3dback.intellijgoarchlint.project.GoArchFileUtils
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType
import com.jetbrains.jsonSchema.ide.JsonSchemaService
import com.jetbrains.jsonSchema.impl.JsonSchemaVersion
import org.jetbrains.yaml.psi.YAMLFile

class GoArchJsonSchemaProvider(
    private val project: Project,
    private val targetVersion: Int,
    private val isDefault: Boolean,
) : JsonSchemaFileProvider {

    private val versionString = "v%d".format(targetVersion)

    private val schemaFileLazy: VirtualFile? by lazy {
        JsonSchemaProviderFactory.getResourceFile(this::class.java, "/schemas/%s.json".format(versionString))
    }

    override fun getSchemaFile() = schemaFileLazy

    override fun getSchemaVersion() = JsonSchemaVersion.SCHEMA_7

    override fun getSchemaType() = SchemaType.embeddedSchema

    override fun getName(): String {
        if (isDefault) {
            return "GoArch (dumb)"
        }

        return "GoArch (%s)".format(versionString)
    }

    override fun isAvailable(file: VirtualFile): Boolean {
        if (!JsonSchemaService.Impl.get(project).isApplicableToFile(file)) {
            return false
        }

        if (!GoArchFileType.INSTANCE.isMyFileType(file)) {
            return false
        }

        return ApplicationManager.getApplication().runReadAction<Boolean> {
            return@runReadAction isVersionedArchFile(file)
        }
    }

    private fun isVersionedArchFile(file: VirtualFile): Boolean {
        val psiManager = PsiManager.getInstance(project)
        val psiFile = psiManager.findFile(file)
        if (psiFile !is YAMLFile) {
            return false
        }

        val version = GoArchFileUtils.readVersion(psiFile)
        if ((version < GoArch.versionMinimum || version > GoArch.versionMaximum) && isDefault) {
            // no check file version, this is fallback invalid json schema
            return true
        }

        return version == targetVersion
    }
}