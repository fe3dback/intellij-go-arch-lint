package com.github.fe3dback.intellijgoarchlint.cmp.integration.executor

import com.github.fe3dback.intellijgoarchlint.cmp.integration.exceptions.ExecutorUntrustedSkippedException
import com.github.fe3dback.intellijgoarchlint.models.Context
import com.intellij.ide.impl.isTrusted

class ExecutorTrustDecorator(
    private val origin: IExecutor,
) : IExecutor {
    override fun execute(ctx: Context, command: String, params: Map<String, String>): String {
        if (!ctx.project.isTrusted()) {
            throw ExecutorUntrustedSkippedException()
        }

        return origin.execute(ctx, command, params)
    }
}