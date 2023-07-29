package com.github.fe3dback.intellijgoarchlint.settings

import com.github.fe3dback.intellijgoarchlint.GoArchLintInstallPath
import com.github.fe3dback.intellijgoarchlint.cmp.integration.installer.ArchLintInstaller
import com.github.fe3dback.intellijgoarchlint.config.file.GoArchIcons
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.selected

class PluginConfiguration(val project: Project) : BoundConfigurable("Go Arch Lint"), SearchableConfigurable {
    private val storageState = project.goArchLintStorage.state

    override fun createPanel(): DialogPanel {
        lateinit var uiEnableIntegrations: JBCheckBox
        // lateinit var uiExecutorTarget: ComboBox<ExecutorTarget>

        val whenHostVerified = PropertyPredicate { storageState.executorHostVerified }
        val whenHostNotVerified = PropertyPredicate { !whenHostVerified() }
        val whenSupportedOS = PropertyPredicate { Features.isExecutorAvailable() }
        val whenNotSupportedOS = PropertyPredicate { !whenSupportedOS() }

        return panel {
            group {

                group("Live integration with IDE") {
                    row {
                        uiEnableIntegrations = checkBox("Execute go-arch-lint in background?")
                            .bindSelected(storageState::enableIntegrations)
                            .component
                    }
                    row {
                        comment("This will execute config validation, code linting,")
                    }
                    row {
                        comment("package mapping, etc.. in background and show results in IDE")
                    }
                }

                group("Integration settings") {

                    // ---------------------------
                    // Target
                    // ---------------------------
                    // todo: docker

//                    row {
//                        text("Target:")
//                        uiExecutorTarget = comboBox(
//                            ExecutorTarget.values().toList()
//                        )
//                            .bindItem(storageState::executorTarget.toNullableProperty())
//                            .component
//                        comment("Usually host faster than docker")
//                    }

                    // ---------------------------
                    // DOCKER Settings
                    // ---------------------------
                    // todo: docker

//                    group("Docker") {
//                        row {
//                            text("Docker image:")
//                            comboBox(
//                                LinterVersion.values().toList()
//                            )
//                                .align(Align.FILL)
//                                .bindItem(storageState::executorDockerVersion.toNullableProperty())
//                        }
//                        row {
//                            comment("What linter version will be used for checking?")
//                            comment("Recommended: latest")
//                        }
//                    }.visibleIf(uiExecutorTarget.selectedValueIs(ExecutorTarget.DOCKER))

                    // ---------------------------
                    // HOST Settings
                    // ---------------------------

                    group("Host") {
                        row {
                            comment("You can download go-arch-lint from IDE, or install it manually")
                        }
                        row {
                            text("Install")
                            comboBox(
                                LinterVersion.values().toList()
                            )
                                .align(Align.FILL)
                                .bindItem(storageState::executorHostInstallVersion.toNullableProperty())
                            button("Download") {
                                ArchLintInstaller.installBinary(project, storageState.executorHostInstallVersion)
                            }
                        }
                        row {
                            comment("Linter will be installed to $GoArchLintInstallPath")
                        }
                        row {
                            text("Binary path:")
                        }

                        row {
                            button("Verify") { project.goArchLintStorage.verifyHostBinary() }
                            textField()
                                .align(Align.FILL)
                                .bindText(storageState::executorHostTmpBinaryPath)
                        }

                        row {
                            icon(GoArchIcons.FILETYPE_ICON)
                            text("Linter: ${storageState.executorHostVerified}")
                                .bold()
                            text("Version: ${storageState.executorHostVerifiedVersion}")
                                .bold()
                        }.visibleIf(whenHostVerified)
                        row {
                            text("Press 'Verify' to apply and check selected linter")
                                .bold()
                        }.visibleIf(whenHostNotVerified)

                        row {
                            comment("Abs file path to binary")
                        }
                    }//.visibleIf(uiExecutorTarget.selectedValueIs(ExecutorTarget.HOST)) // todo: on after docker

                    row {
                        comment("Note: External linter will not be executed if current project is untrusted")
                    }

                    // ---------------------------
                    // Integrations
                    // ---------------------------

                    group("Active integrations") {
                        row {
                            checkBox("Validate config")
                        }
                        row {
                            checkBox("Lint code")
                        }
                        row {
                            checkBox("Show component mappings on packages")
                        }
                    }
                }.visibleIf(uiEnableIntegrations.selected)
            }.visibleIf(whenSupportedOS)

            group {
                row {
                    comment("Currently advanced linter integration supported only on Unix like OS")
                }
            }.visibleIf(whenNotSupportedOS)
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