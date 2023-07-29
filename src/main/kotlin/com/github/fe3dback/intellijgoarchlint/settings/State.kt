package com.github.fe3dback.intellijgoarchlint.settings

import com.github.fe3dback.intellijgoarchlint.GoArchLintInstallPath
import com.github.fe3dback.intellijgoarchlint.models.Version
import com.github.fe3dback.intellijgoarchlint.models.invalidVersionFrom

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
    var enableIntegrations: Boolean = false,
    var executorTarget: ExecutorTarget = ExecutorTarget.HOST,

    var executorDockerVerified: Boolean = false, // todo
    var executorDockerVersion: LinterVersion = LinterVersion.latest, // todo

    var executorHostVerified: Boolean = false,
    var executorHostVerifiedVersion: Version = invalidVersionFrom("?"),
    var executorHostVerifiedBinaryPath: String = "",
    var executorHostTmpBinaryPath: String = GoArchLintInstallPath,
    var executorHostInstallVersion: LinterVersion = LinterVersion.latest,

    var enableSubSelfInspections: Boolean = true, // todo
    var enableSubCodeInspections: Boolean = true, // todo
)