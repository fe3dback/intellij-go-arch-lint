package com.github.fe3dback.intellijgoarchlint.exec

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.diagnostic.Logger
import kotlin.system.measureTimeMillis

class CommandExecutor(private val command: GeneralCommandLine) {
    private val logger: Logger = Logger.getInstance(CommandExecutor::class.java)

    fun execute(): Result {
        val result: Result
        val timings = measureTimeMillis {
            result = innerExecute()
        }

        if (!result.successful()) {
            logger.warn("command '${command}' execution failed after ${timings.toInt()}ms: ${result.error()}")
            return result
        }

        logger.info("docker command executed successfully in ${timings.toInt()}ms")
        return result
    }

    private fun innerExecute(): Result {
        val response = try {
            ExecUtil.execAndGetOutput(command)
        } catch (e: ExecutionException) {
            return error("failed execute docker command: ${e.message}")
        }

        if (response.isCancelled) {
            return error("docker command execution cancelled")
        }

        if (response.stderr != "") {
            return error("docker command failed with stderr: ${response.stderr}")
        }

        if (response.exitCode != 0) {
            return error("docker command exited with error code: ${response.exitCode}")
        }

        return success(response)
    }

    private fun error(errorText: String): Result {
        return Result(null, errorText)
    }

    private fun success(response: ProcessOutput): Result {
        return Result(response, null)
    }
}