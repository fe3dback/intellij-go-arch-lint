package com.github.fe3dback.intellijgoarchlint.file

import com.github.fe3dback.intellijgoarchlint.project.GoArchFileUtils
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.yaml.YAMLLanguage
import javax.swing.Icon

class GoArchFileType private constructor() : LanguageFileType(YAMLLanguage.INSTANCE), FileTypeIdentifiableByVirtualFile {
    companion object {
        val INSTANCE = GoArchFileType()
        const val DEFAULT_EXTENSION = "yml"
    }

    override fun getName(): String {
        return "YAML_GO_ARCH"
    }

    override fun getDescription(): String {
        return "YAML/GoArch"
    }

    override fun getDefaultExtension(): String {
        return DEFAULT_EXTENSION
    }

    override fun getIcon(): Icon {
        return GoArchIcons.FILETYPE_ICON
    }

    override fun isMyFileType(file: VirtualFile): Boolean {
        return GoArchFileUtils.isValid(file)
    }
}