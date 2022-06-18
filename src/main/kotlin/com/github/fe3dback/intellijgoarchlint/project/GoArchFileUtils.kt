package com.github.fe3dback.intellijgoarchlint.project

import com.github.fe3dback.intellijgoarchlint.GoArch
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.impl.StubVirtualFile
import org.jetbrains.yaml.YAMLUtil
import org.jetbrains.yaml.psi.YAMLFile
import java.io.IOException
import java.lang.NumberFormatException
import java.nio.charset.StandardCharsets
import java.util.*

object GoArchFileUtils {
    /** Number of bytes to read when guessing for the file type based on content.  */
    private const val VALIDATE_BYTES_TO_READ = 4096

    /**
     * Read GoArchFile version
     */
    fun readVersion(file: YAMLFile): Int {
        return data(file).version
    }

    /**
     * Is go arch file?
     */
    fun isValid(file: VirtualFile): Boolean {
        if (file is StubVirtualFile) {
            return false
        }

        if (!file.isValid) {
            return false
        }

        val extension = file.extension
        if (!("yml".equals(extension, ignoreCase = true) || "yaml".equals(extension, ignoreCase = true))) {
            return false
        }

        if (file.nameWithoutExtension == ".go-arch-lint") {
            // default well known name
            return true
        }

        // in case of multiple go arch files, or for custom names,
        // we want analyze yaml file for some patterns
        return fileBytesMatchGoArchPattern(file)
    }

    private fun fileBytesMatchGoArchPattern(file: VirtualFile): Boolean {
        return ReadAction.compute<Boolean, Throwable> {
            try {
                file.inputStream.use {
                        inputStream ->
                    val bytes = ByteArray(VALIDATE_BYTES_TO_READ)
                    val n = inputStream.read(bytes, 0, VALIDATE_BYTES_TO_READ)
                    return@compute n > 0 && isGoArchYaml(bytes)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return@compute false
        }
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

                if (line.startsWith("%s:".format(GoArch.specAllow))) {
                    hasAllowTag = true
                    continue
                }

                if (line.contains("%s:".format(GoArch.specAllowDepOnAnyVendor))) {
                    hasDependOnVendorTag = true
                    continue
                }

                if (line.startsWith("%s:".format(GoArch.specComponents))) {
                    hasComponentsTag = true
                    continue
                }

                if (line.startsWith("%s:".format(GoArch.specDeps))) {
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

        return (hasAllowTag && hasDependOnVendorTag) || (hasComponentsTag && hasDependenciesTag)
    }

    private fun data(file: YAMLFile): GoArchFile {
        val topKeys = YAMLUtil.getTopLevelKeys(file)
        val versionProp = topKeys.firstOrNull { prop -> prop.keyText == GoArch.specVersion }

        var version = 0
        if (versionProp != null) {
            version = try {
                versionProp.valueText.format("%d").toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }

        return GoArchFile(version)
    }
}