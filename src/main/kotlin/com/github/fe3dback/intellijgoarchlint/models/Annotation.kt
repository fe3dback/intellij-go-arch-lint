package com.github.fe3dback.intellijgoarchlint.models

class Annotation(
    val message: String,
    val reference: Reference
)

class Reference(
    val valid: Boolean,
    val file: String,
    val line: Int,
    val offset: Int,
)