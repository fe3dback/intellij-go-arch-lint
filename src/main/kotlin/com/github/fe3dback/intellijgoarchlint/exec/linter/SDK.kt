package com.github.fe3dback.intellijgoarchlint.exec.linter

import com.github.fe3dback.intellijgoarchlint.exec.DockerParamsFactory
import com.github.fe3dback.intellijgoarchlint.exec.DockerRunParams
import com.github.fe3dback.intellijgoarchlint.exec.ExecutorProvider
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.intellij.openapi.diagnostic.Logger
import java.lang.reflect.Type

private const val commandSelfInspect = "self-inspect"
private const val commandMapping = "mapping"

private const val linterImageName = "fe3dback/go-arch-lint"
private const val linterImageTag = "dev-1.7.0-rc2"

class SDK(private val context: Context) {
    private val logger: Logger = Logger.getInstance(SDK::class.java)

    fun selfInspect(): Inspection? {
        return parseInto(Inspection::class.java, stdoutOfExecution(prepareCommand(commandSelfInspect)))
    }

    fun mapping(): Mapping? {
        return parseInto(Mapping::class.java, stdoutOfExecution(prepareCommand(commandMapping)))
    }

    private fun <T> parseInto(target: Type, output: String?): T? {
        if (output == null) return null
        if (output == "") return null

        return try {
            Gson().fromJson(output, target)
        } catch (e: JsonSyntaxException) {
            logger.warn("failed parse stdout: ${e.message}")
            null
        }
    }

    private fun stdoutOfExecution(runParams: DockerRunParams): String? {
        val result = ExecutorProvider(runParams).provide().execute()
        if (result.successful()) {
            return result.response().stdout
        }

        return null
    }

    private fun prepareCommand(command: String): DockerRunParams {
        return DockerParamsFactory()
            .withImage(linterImageName, linterImageTag)
            .withAutoRemove(true)
            .withMount(context.projectPath, "/app")
            .withCommand("$command --json --project-path /app --arch-file ${context.goArchFileName}")
            .build()
    }
}