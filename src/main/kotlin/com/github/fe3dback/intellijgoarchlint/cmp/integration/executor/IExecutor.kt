package com.github.fe3dback.intellijgoarchlint.cmp.integration.executor

import com.github.fe3dback.intellijgoarchlint.models.Context

interface IExecutor {
    // execute will run command with some driver(host/docker/etc) and return raw output
    // from stdout (expected that output has json type)
    fun execute(ctx: Context, command: String, params: Map<String, String>): String
}