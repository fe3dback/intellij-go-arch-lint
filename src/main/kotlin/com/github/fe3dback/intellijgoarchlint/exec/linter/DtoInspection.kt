package com.github.fe3dback.intellijgoarchlint.exec.linter

class Inspection(
    val Type: String,
    val Payload: InspectionPayload
)

class InspectionPayload(
    val Notices: List<InspectionNotice>,
)

class InspectionNotice(
    val Text: String,
    val Reference: InspectionReference,
)

class InspectionReference(
    val Valid: Boolean,
    val File: String,
    val Line: Int,
    val Offset: Int,
)