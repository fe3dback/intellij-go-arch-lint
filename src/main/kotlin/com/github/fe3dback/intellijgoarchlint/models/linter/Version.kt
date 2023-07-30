package com.github.fe3dback.intellijgoarchlint.models.linter

import com.google.gson.annotations.SerializedName

class VersionOut(
    @SerializedName("Type")
    val type: String,

    @SerializedName("Payload")
    val payload: VersionPayload
)

class VersionPayload(
    @SerializedName("LinterVersion")
    val version: String,
)
