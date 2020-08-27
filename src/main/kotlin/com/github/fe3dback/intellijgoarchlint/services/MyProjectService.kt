package com.github.fe3dback.intellijgoarchlint.services

import com.intellij.openapi.project.Project
import com.github.fe3dback.intellijgoarchlint.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
