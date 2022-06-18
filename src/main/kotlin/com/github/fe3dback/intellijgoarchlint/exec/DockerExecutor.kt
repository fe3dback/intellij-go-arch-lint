package com.github.fe3dback.intellijgoarchlint.exec

import com.intellij.execution.configurations.GeneralCommandLine

class DockerExecutor(
    private val params: DockerRunParams
) : Executor {
    override fun execute(): Result {
        return CommandExecutor(buildCommand()).execute()
    }

    private fun buildCommand(): GeneralCommandLine {
        val args = mutableListOf<String>()
        args.add("docker")
        args.add("run")

        // need mount?
        if (params.mountVolume != "") {
            args.add("-v")
            args.add(params.mountVolume)
        }

        // clean after run?
        if (params.removeAfterExec) {
            args.add("--rm")
        }

        // what image need to run
        args.add("${params.imageName}:${params.imageTag}")

        // add command execution params inside container
        params.command.split(" ").forEach {
            args.add(it)
        }

        return GeneralCommandLine(args.toList())
    }
}