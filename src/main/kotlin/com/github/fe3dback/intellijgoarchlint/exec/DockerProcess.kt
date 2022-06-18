package com.github.fe3dback.intellijgoarchlint.exec

import com.intellij.openapi.progress.ProgressIndicatorProvider

class DockerProcess(private val inner: Executor) : Executor {
    override fun execute(): Result {
        ProgressIndicatorProvider.getGlobalProgressIndicator().run {
            return inner.execute()
        }
    }
}