package com.github.fe3dback.intellijgoarchlint.cmp.integration.executor

import com.github.fe3dback.intellijgoarchlint.models.Context
import com.intellij.openapi.progress.ProgressIndicatorProvider

class ExecutorProcessIndicatorDecorator(
    private val origin: IExecutor,
) : IExecutor {
    override fun execute(ctx: Context, command: String, params: Map<String, String>): String {
        ProgressIndicatorProvider.getGlobalProgressIndicator().run {
            return origin.execute(ctx, command, params)
        }
    }
}