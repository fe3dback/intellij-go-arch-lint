package com.github.fe3dback.intellijgoarchlint.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class Features {
    companion object {
        fun isConfigLintEnabled(project: Project): Boolean {
            if (!project.goArchLintStorage.state.enableIntegrations) return false
            if (!project.goArchLintStorage.state.enableSubSelfInspections) return false

            return true
        }

        fun canShowInstallPopupOnIDEStart(project: Project): Boolean {
            if (!isExecutorAvailable()) return false // can`t be installed anyway
            if (!project.goArchLintStorage.state.allowShowingInstallPopup) return false // suppressed
            if (project.goArchLintStorage.state.executorHostVerifiedBinaryPath != "") return false // already configured

            return true
        }

        fun isExecutorAvailable(): Boolean {
            // todo: windows/mac/wsl support?
            return SystemInfo.isLinux
        }

        fun isExecutorDockerAvailable(project: Project): Boolean {
            if (!isExecutorAvailable()) return false

            // todo: check that docker installed and image ready
            // check should be done inside config, here we only compare storage variable
            return project.goArchLintStorage.state.executorTarget == ExecutorTarget.DOCKER
        }

        fun isExecutorHostAvailable(project: Project): Boolean {
            if (!isExecutorAvailable()) return false
            if (project.goArchLintStorage.state.executorTarget != ExecutorTarget.HOST) return false
            if (project.goArchLintStorage.state.executorHostVerifiedBinaryPath == "") return false

            // todo: check that binary installed and path is correct
            // check should be done inside config, here we only compare storage variable
            return true
        }
    }
}