package com.github.fe3dback.intellijgoarchlint.cmp.notifications

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project

class CommonNotification {
    companion object {
        fun show(project: Project, message: String, type: NotificationType) {
            notify(project, message, type)
        }

        fun showLater(project: Project, message: String, type: NotificationType, modalityState: ModalityState) {
            ApplicationManager.getApplication().invokeLater({
                notify(project, message, type)
            }, modalityState)
        }

        private fun notify(project: Project, message: String, type: NotificationType) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Go-Arch-Lint Untrusted")
                .createNotification(message, type)
                .notify(project)
        }
    }
}