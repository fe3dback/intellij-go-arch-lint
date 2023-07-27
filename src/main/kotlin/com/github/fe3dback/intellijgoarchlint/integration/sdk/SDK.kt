package com.github.fe3dback.intellijgoarchlint.integration.sdk

import com.github.fe3dback.intellijgoarchlint.integration.GoArchSDK
import com.github.fe3dback.intellijgoarchlint.integration.exceptions.ExecutorNotAvailableException
import com.github.fe3dback.intellijgoarchlint.integration.executor.Executor
import com.github.fe3dback.intellijgoarchlint.models.Annotation
import com.github.fe3dback.intellijgoarchlint.models.Context
import com.github.fe3dback.intellijgoarchlint.models.Reference
import com.github.fe3dback.intellijgoarchlint.models.linter.SelfInspectOut
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.intellij.openapi.diagnostic.Logger

class SDK(
    private val executor: Executor,
) : GoArchSDK {
    private val logger: Logger = Logger.getInstance(SDK::class.java)

    private enum class ResultType {
        OK,
        EXECUTOR_OFFLINE,
        PARSE_ERROR,
        EXECUTE_ERROR,
    }

    override fun configIssues(ctx: Context): List<Annotation> {
        val (response, type, errorMessage) = execute<SelfInspectOut>(
            LintCommandSelfInspect, mapOf(
                "project-path" to ctx.projectPath,
                "arch-file" to ctx.configName,
                "json" to "",
            )
        )

        if (errorMessage != null && errorMessage != "") {
            logger.warn(errorMessage)
        }

        when (type) {
            ResultType.PARSE_ERROR -> throw commandException(ctx, LintCommandSelfInspect, errorMessage ?: "parse error")
            ResultType.EXECUTE_ERROR -> throw commandException(
                ctx,
                LintCommandSelfInspect,
                errorMessage ?: "execute error"
            )

            ResultType.EXECUTOR_OFFLINE -> return emptyList()
            ResultType.OK -> return response!!.payload.notices.map {
                Annotation(
                    it.text,
                    Reference(
                        it.reference.valid,
                        it.reference.file,
                        it.reference.line,
                        it.reference.offset,
                    )
                )
            }
        }
    }

    private fun commandException(ctx: Context, cmd: String, message: String): Exception {
        return Exception("Command '$cmd' execution failed: $message; (conf=${ctx.configName}, project=${ctx.projectPath})")
    }

    private inline fun <reified T> execute(cmd: String, args: Map<String, String>): Triple<T?, ResultType, String?> {
        try {
            val responseJson = executor.execute(cmd, args)
            val data: T = parseJson(responseJson)
            return Triple(data, ResultType.OK, null)
        } catch (e: ExecutorNotAvailableException) {
            return Triple(null, ResultType.EXECUTOR_OFFLINE, e.message)
        } catch (e: JsonSyntaxException) {
            return Triple(null, ResultType.PARSE_ERROR, e.message)
        } catch (e: Exception) {
            return Triple(null, ResultType.EXECUTE_ERROR, e.message)
        }
    }

    private inline fun <reified T : Any> parseJson(responseJson: String): T {
        try {
            return Gson().fromJson(responseJson, T::class.java)
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException(
                "Failed parse output: ${e.message}. Out: ${
                    responseJson.substring(
                        0,
                        100
                    )
                }"
            )
        }
    }
}