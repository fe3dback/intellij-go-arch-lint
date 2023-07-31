package com.github.fe3dback.intellijgoarchlint.config.file

import com.github.fe3dback.intellijgoarchlint.common.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.project.GoArchFileUtils
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.yaml.YAMLLanguage
import javax.swing.Icon

class GoArchFileType private constructor() : LanguageFileType(YAMLLanguage.INSTANCE),
    FileTypeIdentifiableByVirtualFile {
    companion object {
        val INSTANCE = GoArchFileType()
        const val DEFAULT_EXTENSION = "yml"
    }

    override fun getName(): String = "YAML_GO_ARCH"
    override fun getDescription(): String = "YAML/GoArch"
    override fun getDefaultExtension(): String = DEFAULT_EXTENSION
    override fun getIcon(): Icon = GoArchIcons.FILETYPE_ICON
    override fun isMyFileType(file: VirtualFile): Boolean = GoArchFileUtils.isValid(file)
}