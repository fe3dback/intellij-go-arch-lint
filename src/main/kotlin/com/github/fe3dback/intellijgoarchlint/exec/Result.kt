package com.github.fe3dback.intellijgoarchlint.exec

import com.intellij.execution.process.ProcessOutput

class Result(
    private val possibleResponse: ProcessOutput?,
    private val possibleError: String?
) {
    fun successful(): Boolean {
        return possibleError == null
    }

    fun response(): ProcessOutput {
        return possibleResponse!!
    }

    fun error(): String {
        return possibleError!!
    }
}
