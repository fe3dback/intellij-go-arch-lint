package com.github.fe3dback.intellijgoarchlint.cmp.integration.executor

import com.github.fe3dback.intellijgoarchlint.cmp.integration.exceptions.ExecutorCancelledException
import com.github.fe3dback.intellijgoarchlint.cmp.integration.exceptions.ExecutorNotAvailableException
import com.github.fe3dback.intellijgoarchlint.models.Context
import com.github.fe3dback.intellijgoarchlint.settings.goArchLintStorage
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil

class ExecutorHost : IExecutor {
    override fun execute(ctx: Context, command: String, params: Map<String, String>): String {
        val shellBuff = mutableListOf(ctx.project.goArchLintStorage.state.executorHostVerifiedBinaryPath)
        shellBuff.add(command)

        params.forEach {
            if (it.value == "") {
                shellBuff.add("--${it.key}")
            } else {
                shellBuff.add("--${it.key}=${it.value}")
            }
        }

        val cmd = GeneralCommandLine(shellBuff)
            .withCharset(Charsets.UTF_8)

        val response = try {
            ExecUtil.execAndGetOutput(cmd, 5000)
        } catch (e: Exception) {
            if (e.message != null && e.message!!.contains("No such file or directory")) {
                throw ExecutorNotAvailableException()
            }

            throw e
        }

        if (response.isCancelled) {
            throw ExecutorCancelledException()
        }

        if (response.isTimeout) {
            throw ExecutorCancelledException()
        }

        return response.stdout
    }
}