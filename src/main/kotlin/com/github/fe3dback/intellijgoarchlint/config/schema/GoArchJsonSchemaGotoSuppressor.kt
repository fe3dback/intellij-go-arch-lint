package com.github.fe3dback.intellijgoarchlint.config.schema

import com.github.fe3dback.intellijgoarchlint.config.file.GoArchFileType
import com.intellij.psi.PsiElement
import com.jetbrains.jsonSchema.extension.JsonSchemaGotoDeclarationSuppressor

class GoArchJsonSchemaGotoSuppressor : JsonSchemaGotoDeclarationSuppressor {
    override fun shouldSuppressGtd(psiElement: PsiElement?): Boolean {
        val parentFile = psiElement?.containingFile?.virtualFile ?: return false

        return GoArchFileType.INSTANCE.isMyFileType(parentFile)
    }
}