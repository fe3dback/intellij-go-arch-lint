package com.github.fe3dback.intellijgoarchlint.cmp.integration.installer

import com.github.fe3dback.intellijgoarchlint.cmp.notifications.CommonNotification
import com.github.fe3dback.intellijgoarchlint.cmp.notifications.UnableInUntrustedProjectNotification
import com.github.fe3dback.intellijgoarchlint.models.Version
import com.github.fe3dback.intellijgoarchlint.models.invalidVersionFrom
import com.github.fe3dback.intellijgoarchlint.models.linter.VersionOut
import com.github.fe3dback.intellijgoarchlint.models.parseRawVersion
import com.google.gson.Gson
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil
import com.intellij.ide.impl.isTrusted
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project

class ArchLintVerifier {
    companion object {
        fun verifyBinary(project: Project, binaryPath: String): Version {
            val invalid = invalidVersionFrom("?")

            if (!project.isTrusted()) {
                UnableInUntrustedProjectNotification.show(project, NotificationType.INFORMATION)
                return invalid
            }

            val cmd = GeneralCommandLine(listOf(binaryPath, "version", "--json"))
                .withCharset(Charsets.UTF_8)

            val response = ExecUtil.execAndGetOutput(cmd, 5000)
            if (response.exitCode != 0) {
                CommonNotification.showLater(
                    project,
                    "Linter '${binaryPath}' not respond as expected",
                    NotificationType.WARNING,
                    ModalityState.any()
                )
                return invalid
            }

            val out = try {
                Gson().fromJson(response.stdout, VersionOut::class.java)
            } catch (e: Exception) {
                CommonNotification.showLater(
                    project,
                    "Failed parse '${binaryPath} version' response: ${e.message}",
                    NotificationType.WARNING,
                    ModalityState.any()
                )
                return invalid
            }

            return parseRawVersion(out.payload.version)
        }
    }
}