package com.github.fe3dback.intellijgoarchlint.cmp.notifications

import com.github.fe3dback.intellijgoarchlint.cmp.actions.GotoIntegrationSettingsAction
import com.github.fe3dback.intellijgoarchlint.common.GoArchIcons
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class IntegrationMissConfiguredNotification {
    companion object {
        fun show(project: Project, message: String) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Go-Arch-Lint MissConfiguration")
                .createNotification(message, NotificationType.WARNING)
                .setIcon(GoArchIcons.FILETYPE_ICON)
                .addAction(GotoIntegrationSettingsAction())
                .notify(project)
        }
    }
}