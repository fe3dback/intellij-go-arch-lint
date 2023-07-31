package com.github.fe3dback.intellijgoarchlint.cmp.actions

import com.github.fe3dback.intellijgoarchlint.common.GoArchIcons
import com.github.fe3dback.intellijgoarchlint.settings.PluginConfiguration
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware

class GotoIntegrationSettingsAction : AnAction(
    "Configure",
    "Change go-arch-lint settings",
    GoArchIcons.FILETYPE_ICON,
), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        ShowSettingsUtil.getInstance().showSettingsDialog(project, PluginConfiguration::class.java)
    }
}