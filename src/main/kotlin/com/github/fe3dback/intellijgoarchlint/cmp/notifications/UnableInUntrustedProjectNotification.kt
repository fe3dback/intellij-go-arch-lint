package com.github.fe3dback.intellijgoarchlint.cmp.notifications

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class UnableInUntrustedProjectNotification {
    companion object {
        fun show(project: Project, type: NotificationType) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Go-Arch-Lint Untrusted")
                .createNotification("Can`t perform this action in untrusted project", type)
                .notify(project)
        }
    }
}