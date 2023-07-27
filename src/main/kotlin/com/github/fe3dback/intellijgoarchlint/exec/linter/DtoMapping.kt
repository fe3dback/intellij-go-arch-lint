package com.github.fe3dback.intellijgoarchlint.exec.linter

import com.github.fe3dback.intellijgoarchlint.models.linter.InspectionReference

class Mapping(
    val Type: String,
    val Payload: MappingPayload
)

class MappingPayload(
    val MappingList: List<FileMapping>,
)

class FileMapping(
    val FileName: String,
    val ComponentName: InspectionReference,
)