package com.github.fe3dback.intellijgoarchlint.cmp.integration.installer

import com.github.fe3dback.intellijgoarchlint.cmp.notifications.UnableInUntrustedProjectNotification
import com.github.fe3dback.intellijgoarchlint.settings.LinterVersion
import com.intellij.ide.impl.isTrusted
import com.intellij.notification.NotificationType
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class ArchLintInstaller {
    companion object {
        fun installBinary(project: Project, version: LinterVersion) {
            object : Task.Backgroundable(project, "Installing go-arch-lint ${version}...") {
                override fun shouldStartInBackground(): Boolean = false
                override fun run(indicator: ProgressIndicator) {
                    // todo: auto installed
                    if (!project.isTrusted()) {
                        UnableInUntrustedProjectNotification.show(project, NotificationType.INFORMATION)
                        return
                    }

                    // todo: replace with real code
                    indicator.isIndeterminate = true
                    Thread.sleep(1_000)
                    indicator.text = "Downloading (XMB / YMB)..."

                    //
                    // project.showBalloon(result.error, NotificationType.ERROR)
                }
            }.queue()
        }
    }
}