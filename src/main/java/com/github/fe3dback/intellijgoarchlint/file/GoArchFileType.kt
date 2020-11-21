package com.github.fe3dback.intellijgoarchlint.file

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.ex.FileTypeIdentifiableByVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.impl.StubVirtualFile
import org.jetbrains.yaml.YAMLLanguage
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.swing.Icon

class GoArchFileType private constructor() : LanguageFileType(YAMLLanguage.INSTANCE), FileTypeIdentifiableByVirtualFile {
    companion object {
        val INSTANCE = GoArchFileType()
        const val DEFAULT_EXTENSION = "yml"

        /** Number of bytes to read when guessing for the file type based on content.  */
        private const val BYTES_TO_READ = 4096
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
        if (file is StubVirtualFile) {
            return false // Helps New -> File get correct file type
        }

        if (!file.isValid) {
            return false
        }

        val extension = file.extension
        if (!("yml".equals(extension, ignoreCase = true) || "yaml".equals(extension, ignoreCase = true))) {
            return false
        }

        try {
            file.inputStream.use {
                inputStream ->
                val bytes = ByteArray(BYTES_TO_READ)
                val n = inputStream.read(bytes, 0, BYTES_TO_READ)
                return n > 0 && isGoArchYaml(bytes)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * Guess whether the file is a GoArch YAML file from a subset of the file content.
     *
     * @param bytes the bytes to check.
     * @return true if the file is a GoArch YAML file, otherwise, false.
     */
    private fun isGoArchYaml(bytes: ByteArray): Boolean {
        var hasComponentsTag = false
        var hasDependenciesTag = false
        var hasAllowTag = false
        var hasDependOnVendorTag = false

        Scanner(String(bytes, StandardCharsets.UTF_8)).use { scanner ->
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()

                if (line.startsWith("allow:")) {
                    hasAllowTag = true
                    continue
                }

                if (line.contains("depOnAnyVendor:")) {
                    hasDependOnVendorTag = true
                    continue
                }

                if (line.startsWith("components:")) {
                    hasComponentsTag = true
                    continue
                }

                if (line.startsWith("deps:")) {
                    hasDependenciesTag = true
                    continue
                }

                if (hasComponentsTag && hasDependenciesTag) {
                    break
                }

                if (hasAllowTag && hasDependOnVendorTag) {
                    break
                }
            }
        }

        return hasAllowTag && hasDependOnVendorTag || hasComponentsTag && hasDependenciesTag
    }
}