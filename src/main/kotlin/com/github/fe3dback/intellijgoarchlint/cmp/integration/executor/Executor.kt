package com.github.fe3dback.intellijgoarchlint.cmp.integration.executor

import com.github.fe3dback.intellijgoarchlint.cmp.integration.exceptions.ExecutorNotInstalled
import com.github.fe3dback.intellijgoarchlint.models.Context
import com.github.fe3dback.intellijgoarchlint.settings.Features

class Executor(
    private val hostExecutor: IExecutor,
    private val dockerExecutor: IExecutor,
) : IExecutor {
    override fun execute(ctx: Context, command: String, params: Map<String, String>): String {
        if (Features.isExecutorHostAvailable(ctx.project)) {
            return hostExecutor.execute(ctx, command, params)
        }

        if (Features.isExecutorDockerAvailable(ctx.project)) {
            return dockerExecutor.execute(ctx, command, params)
        }

        throw ExecutorNotInstalled()
    }
}