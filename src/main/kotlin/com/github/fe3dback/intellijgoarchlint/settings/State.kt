package com.github.fe3dback.intellijgoarchlint.settings

import com.github.fe3dback.intellijgoarchlint.models.Version
import com.github.fe3dback.intellijgoarchlint.models.invalidVersionFrom

enum class ExecutorTarget {
    HOST,
    DOCKER, // todo
}

enum class LinterVersion { // todo: install
    latest,
}

data class State(
    var allowShowingInstallPopup: Boolean = true,

    // enable all other advanced integrations between go-arch-lint and IDE
    var enableIntegrations: Boolean = true,
    var executorTarget: ExecutorTarget = ExecutorTarget.HOST,

    var executorHostVerifiedVersion: Version = invalidVersionFrom("?"),
    var executorHostVerifiedBinaryPath: String = "",

    var enableSubSelfInspections: Boolean = true,
)