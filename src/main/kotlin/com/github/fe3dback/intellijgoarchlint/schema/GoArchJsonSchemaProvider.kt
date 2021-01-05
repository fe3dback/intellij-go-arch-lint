package com.github.fe3dback.intellijgoarchlint.schema

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.github.fe3dback.intellijgoarchlint.file.GoArchFileType
import com.github.fe3dback.intellijgoarchlint.project.GoArchFileUtils
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.util.ResourceUtil
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.SchemaType
import com.jetbrains.jsonSchema.impl.JsonSchemaVersion
import org.jetbrains.yaml.psi.YAMLFile
import java.net.URL
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.ThrowableComputable
import com.jetbrains.jsonSchema.ide.JsonSchemaService
import org.jetbrains.concurrency.await
import java.util.concurrent.Callable


class GoArchJsonSchemaProvider(
    private val project: Project,
    private val targetVersion: Int,
    private val isDefault: Boolean,
) : JsonSchemaFileProvider {
    override fun isAvailable(file: VirtualFile): Boolean {
        if (!JsonSchemaService.Impl.get(project).isApplicableToFile(file)) {
            return false
        }

        return ApplicationManager.getApplication().runReadAction<Boolean> {
            isAvailableSync(file)
        }
    }

    private fun isAvailableSync(file: VirtualFile): Boolean {
        if (!GoArchFileType.INSTANCE.isMyFileType(file)) {
            return false
        }

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

    override fun getSchemaVersion(): JsonSchemaVersion {
        return JsonSchemaVersion.SCHEMA_7
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.embeddedSchema
    }

    override fun getName(): String {
        if (isDefault) {
            return "GoArch (dumb)"
        }

        return "GoArch (v%d)".format(targetVersion)
    }

    override fun getSchemaFile(): VirtualFile? {
        val url: URL = ResourceUtil.getResource(
            javaClass,
            "schemas",
            "v%d.json".format(targetVersion)
        )
        return VfsUtil.findFileByURL(url)
    }
}