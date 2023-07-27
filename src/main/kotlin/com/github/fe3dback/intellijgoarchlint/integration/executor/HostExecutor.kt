package com.github.fe3dback.intellijgoarchlint.integration.executor

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil

class HostExecutor : Executor {
    // todo: decorate with timings, messages, clock, etc..
    override fun execute(command: String, params: Map<String, String>): String {
        // todo: path resolving (replace binary path here)
        val shellBuff = mutableListOf("/home/neo/go/bin/go-arch-lint")
        shellBuff.add(command)

        params.forEach {
            if (it.value == "") {
                shellBuff.add("--${it.key}")
            } else {
                shellBuff.add("--${it.key}=${it.value}")
            }
        }

        val cmd = GeneralCommandLine(shellBuff)
        val response = ExecUtil.execAndGetOutput(cmd)

        // todo: check cancelled
        // todo: check stderr
        // todo: check exitcode

        return response.stdout
    }
}