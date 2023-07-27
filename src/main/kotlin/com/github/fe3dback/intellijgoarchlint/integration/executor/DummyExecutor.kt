package com.github.fe3dback.intellijgoarchlint.integration.executor

import com.intellij.openapi.diagnostic.Logger

class DummyExecutor : Executor {
    private val logger: Logger = Logger.getInstance(DummyExecutor::class.java)

    override fun execute(command: String, params: Map<String, String>): String {
        logger.info("Dummy executed command '$command' with args '$params'")

        return """
            {
              "Type": "models.SelfInspect",
              "Payload": {
                "ModuleName": "github.com/fe3dback/go-arch-lint",
                "RootDirectory": "/home/neo/go/src/github.com/fe3dback/go-arch-lint",
                "LinterVersion": "dev",
                "Notices": [
                  {
                    "Text": "unknown component 'view'",
                    "Reference": {
                      "Valid": true,
                      "File": "/home/neo/go/src/github.com/fe3dback/go-arch-lint/.go-arch-lint.yml",
                      "Line": 49,
                      "Offset": 9
                    }
                  }
                ],
                "Suggestions": []
              }
            }
        """.trimIndent()
    }
}