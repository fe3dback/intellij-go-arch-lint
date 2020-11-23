package com.github.fe3dback.intellijgoarchlint.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.util.ResourceUtil
import com.jetbrains.jsonSchema.extension.JsonLikePsiWalker
import com.jetbrains.jsonSchema.ide.JsonSchemaService
import com.jetbrains.jsonSchema.impl.JsonComplianceCheckerOptions
import com.jetbrains.jsonSchema.impl.JsonSchemaComplianceChecker
import org.jetbrains.yaml.psi.YAMLFile
import java.net.URL


class SchemeInspection : LocalInspectionTool() {
    override fun buildVisitor(
            holder: ProblemsHolder,
            isOnTheFly: Boolean,
            session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return createVisitor(holder, session)
    }

    private fun createVisitor(holder: ProblemsHolder, session: LocalInspectionToolSession): PsiElementVisitor {
        val file = holder.file
        val documents = (file as YAMLFile).documents
        if (documents.size != 1) return PsiElementVisitor.EMPTY_VISITOR

        val root = documents[0].topLevelValue ?: return PsiElementVisitor.EMPTY_VISITOR
        val service = JsonSchemaService.Impl.get(file.getProject())

        // todo: current goArch have only version=1, but later we can get version from YAML keyValue param
        val url: URL = ResourceUtil.getResource(javaClass, "schemas", "v1.json")
        val virtualFile = VfsUtil.findFileByURL(url)

        val schema = service.getSchemaObjectForSchemaFile(virtualFile!!)

        return object : org.jetbrains.yaml.psi.YamlPsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element !== root) return
                val walker = JsonLikePsiWalker.getWalker(element, schema) ?: return
                val options = JsonComplianceCheckerOptions(false)
                JsonSchemaComplianceChecker(schema!!, holder, walker, session, options).annotate(element)
            }
        }
    }
}