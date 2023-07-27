package com.github.fe3dback.intellijgoarchlint.settings

import com.github.fe3dback.intellijgoarchlint.GoArchLintInstallPath

enum class ExecutorTarget {
    HOST,
    DOCKER,
}

enum class LinterVersion {
    v1_08,
    v1_09,
    v1_10,
    v1_11,
    latest,
}

data class State(
    // enable all other advanced integrations between go-arch-lint and IDE
    var enableIntegrations: Boolean = true,
    var executorTarget: ExecutorTarget = ExecutorTarget.HOST,
    var executorVersion: LinterVersion = LinterVersion.latest,
    var executorHostBinaryPath: String = GoArchLintInstallPath,
    var executorHostInstallVersion: LinterVersion = LinterVersion.latest,

    var enableSubSelfInspections: Boolean = true,
    var enableSubCodeInspections: Boolean = true,
)