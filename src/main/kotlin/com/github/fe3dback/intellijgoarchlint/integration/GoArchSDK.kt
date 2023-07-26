package com.github.fe3dback.intellijgoarchlint.integration

import com.github.fe3dback.intellijgoarchlint.models.Annotation

interface GoArchSDK {
    fun configIssues(): List<Annotation>
}