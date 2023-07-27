package com.github.fe3dback.intellijgoarchlint.integration.executor

interface Executor {
    // execute will run command with some driver(host/docker/etc) and return raw output
    // from stdout (expected that output has json type)
    fun execute(command: String, params: Map<String, String>): String
}