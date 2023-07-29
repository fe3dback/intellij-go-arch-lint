package com.github.fe3dback.intellijgoarchlint.cmp.integration.sdk

import com.github.fe3dback.intellijgoarchlint.cmp.integration.GoArchSDK
import com.github.fe3dback.intellijgoarchlint.cmp.integration.exceptions.ExecutorCancelledException
import com.github.fe3dback.intellijgoarchlint.cmp.integration.exceptions.ExecutorNotAvailableException
import com.github.fe3dback.intellijgoarchlint.cmp.integration.exceptions.ExecutorNotInstalled
import com.github.fe3dback.intellijgoarchlint.cmp.integration.executor.IExecutor
import com.github.fe3dback.intellijgoarchlint.cmp.notifications.IntegrationMissConfiguredNotification
import com.github.fe3dback.intellijgoarchlint.models.Annotation
import com.github.fe3dback.intellijgoarchlint.models.Context
import com.github.fe3dback.intellijgoarchlint.models.Reference
import com.github.fe3dback.intellijgoarchlint.models.linter.SelfInspectOut
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.intellij.openapi.diagnostic.Logger

class SDK(
    private val executor: IExecutor,
) : GoArchSDK {
    private val logger: Logger = Logger.getInstance(SDK::class.java)

    override fun configIssues(ctx: Context): List<Annotation> {
        return exec(ctx, LintCommandSelfInspect, emptyList()) {
            execSelfInspect(ctx).payload.notices.map {
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

    private fun <T> exec(ctx: Context, cmd: String, default: T, u: () -> T): T {
        return try {
            u()
        } catch (e: ExecutorNotInstalled) {
            // not confirmed/verified or installed (is not error, because it totally normal in first run)
            return default
        } catch (e: ExecutorNotAvailableException) {
            // executor returns unexpected "binary not found" error (possible binary file deleted/moved)
            // in this case, we need promote user to reconfigure project settings
            IntegrationMissConfiguredNotification.show(ctx.project, extractWarningText(ctx, cmd, e))
            return default
        } catch (e: ExecutorCancelledException) {
            // just cancelled by ide/user, its ok
            return default
        } catch (e: Exception) {
            // all other errors, at least we need to log it somewhere
            logger.warn(extractWarningText(ctx, cmd, e))
            return default
        }
    }

    private fun execSelfInspect(ctx: Context) =
        execute<SelfInspectOut>(
            ctx, LintCommandSelfInspect, mapOf(
                LintFlagProjectPath to ctx.projectPath,
                LintFlagArchFileName to ctx.configName,
                LintFlagOutputJSON to "",
            )
        )

    private fun extractWarningText(ctx: Context, cmd: String, e: Exception): String {
        return "Command '$cmd' execution failed: ${e.message}; (conf=${ctx.configName}, project=${ctx.projectPath})"
    }

    private inline fun <reified T> execute(ctx: Context, cmd: String, args: Map<String, String>): T {
        val responseJson = executor.execute(ctx, cmd, args)
        return parseJson(responseJson)
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