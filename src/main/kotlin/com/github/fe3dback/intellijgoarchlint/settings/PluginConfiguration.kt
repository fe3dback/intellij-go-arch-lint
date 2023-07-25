package com.github.fe3dback.intellijgoarchlint.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

class PluginConfiguration(val project: Project) : BoundConfigurable("Go Arch Lint"), SearchableConfigurable {
    private val storageState = project.goArchLintStorage.state

    override fun createPanel(): DialogPanel {
        return panel {
            group {
                group("Live integration with IDE") {
                    row {
                        checkBox("Execute go-arch-lint in background?")
                    }
                    row {
                        comment("This will execute config validation, code linting, package mapping, etc.. in background and show results in IDE")
                    }
                    row {
                        text("Target:")
                        dropDownLink("Host", arrayListOf("Docker", "Host"))
                        comment("Usually host faster than docker")
                    }
                    group("Target: Docker settings") {
                        row {
                            text("Docker image:")
                            dropDownLink("latest", arrayListOf("v1.10.0", "v1.11.0", "latest"))
                        }
                        row {
                            comment("What linter version will be used for checking?")
                            comment("Recommended: latest")
                        }
                    }
                    group("Target: Host settings") {
                        row {
                            text("Binary path:")
                            textField().bindText(storageState::testStringField)
                            comment("Abs file path to binary")
                        }
                        row {
                            text("Install")
                            dropDownLink("latest", arrayListOf("v1.10.0", "v1.11.0", "latest"))
                            button("Download") {}
                        }
                        row {
                            comment("You can download go-arch-lint from IDE, or install it manually")
                        }
                    }
                }

                group("Integration Types") {
                    row {
                        checkBox("Validate config")
                    }
                }
            }
        }
    }

    override fun getId(): String = "com.github.fe3dback.go-arch-lint.settings"

    override fun isModified(): Boolean {
        if (super<BoundConfigurable>.isModified()) return true
        return project.goArchLintStorage.state != storageState
    }

    override fun apply() {
        super.apply()
        project.goArchLintStorage.state = storageState
    }
}