package com.github.fe3dback.intellijgoarchlint.models.linter

import com.google.gson.annotations.SerializedName

class SelfInspectOut(
    @SerializedName("Type")
    val type: String,

    @SerializedName("Payload")
    val payload: InspectionPayload
)

class InspectionPayload(
    @SerializedName("Notices")
    val notices: List<InspectionNotice>,
)

class InspectionNotice(
    @SerializedName("Text")
    val text: String,

    @SerializedName("Reference")
    val reference: InspectionReference,
)

class InspectionReference(
    @SerializedName("Valid")
    val valid: Boolean,

    @SerializedName("File")
    val file: String,

    @SerializedName("Line")
    val line: Int,

    @SerializedName("Offset")
    val offset: Int,
)
