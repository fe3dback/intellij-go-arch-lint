package com.github.fe3dback.intellijgoarchlint.models

data class Reference(
    val valid: Boolean,
    val file: String,
    val line: Int,
    val offset: Int,
)