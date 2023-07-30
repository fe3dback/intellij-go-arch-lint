package com.github.fe3dback.intellijgoarchlint.settings.startup

import com.github.fe3dback.intellijgoarchlint.cmp.notifications.ConfigurationRequiredNotification
import com.github.fe3dback.intellijgoarchlint.settings.Features
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class ConfigurationCheck : StartupActivity.DumbAware {
    override fun runActivity(project: Project) {
        ProgressManager.getInstance()
            .run(object : Task.Backgroundable(project, "Checking settings") {
                override fun run(pi: ProgressIndicator) {
                    pi.isIndeterminate = true

                    // todo: bad code, need normal solution here
                    // why: currently runActivity executed before plugin settings is loaded
                    //      from storage, and we can`t check "allowShowingInstallPopup" here
                    //      and skip notification, if it not needed
                    Thread.sleep(5_000)

                    if (!Features.canShowInstallPopupOnIDEStart(project)) return

                    ConfigurationRequiredNotification.show(project)
                }
            })
    }
}