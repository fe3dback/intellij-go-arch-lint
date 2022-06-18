package com.github.fe3dback.intellijgoarchlint.exec

class ExecutorProvider(params: DockerRunParams) {
    private val executor = DockerProcess(
        DockerExecutor(params)
    )

    fun provide(): Executor {
        return executor
    }
}