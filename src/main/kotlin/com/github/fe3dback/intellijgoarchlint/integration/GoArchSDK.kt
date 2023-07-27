package com.github.fe3dback.intellijgoarchlint.integration

import com.github.fe3dback.intellijgoarchlint.models.Annotation
import com.github.fe3dback.intellijgoarchlint.models.Context

interface GoArchSDK {
    fun configIssues(ctx: Context): List<Annotation>
}