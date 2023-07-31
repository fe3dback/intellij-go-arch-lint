package com.github.fe3dback.intellijgoarchlint.cmp.notifications

import com.github.fe3dback.intellijgoarchlint.cmp.actions.GotoIntegrationSettingsAction
import com.github.fe3dback.intellijgoarchlint.common.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.settings.goArchLintStorage
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class ConfigurationRequiredNotification {
    companion object {
        fun show(project: Project) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Go-Arch-Lint MissConfiguration")
                .createNotification(
                    "Configure go-arch-lint",
                    "Linter can be configured for advanced integrations with IDE",
                    NotificationType.INFORMATION
                )
                .setIcon(GoArchIcons.FILETYPE_ICON)
                .apply {
                    this.addAction(GotoIntegrationSettingsAction())
                    this.addAction(NotificationAction.createSimple("Don't show for this project") {
                        project.goArchLintStorage.state.allowShowingInstallPopup = false
                        this.expire()
                    })
                }
                .notify(project)
        }
    }
}