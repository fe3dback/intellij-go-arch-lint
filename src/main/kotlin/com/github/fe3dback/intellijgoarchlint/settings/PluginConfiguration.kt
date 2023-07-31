package com.github.fe3dback.intellijgoarchlint.settings

import com.github.fe3dback.intellijgoarchlint.common.GoArchIcons
import com.intellij.icons.AllIcons
import com.intellij.ide.impl.isTrusted
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.layout.selected
import java.awt.datatransfer.StringSelection
import javax.swing.JEditorPane

class PluginConfiguration(val project: Project) : BoundConfigurable("Go Arch Lint"), SearchableConfigurable {
    private val storage = project.goArchLintStorage
    private val storageState = storage.state
    private var asyncModified = false

    override fun createPanel(): DialogPanel {
        lateinit var uiEnableIntegrations: JBCheckBox
        lateinit var uiLinterHostVerifySuccessRow: Row
        lateinit var uiLinterHostVerifyFailRow: Row
        lateinit var uiLinterHostVerifyVersion: Cell<JEditorPane>

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

                    row {
                        icon(AllIcons.General.Warning)
                        comment("External integrations will not be used, because project is untrusted").bold()
                    }.visible(!project.isTrusted())
                }

                group("Linter") {

                    // ---------------------------
                    // HOST: Choose
                    // ---------------------------

                    group("Choose") {
                        row {
                            text("Path to go-arch-lint:")
                        }
                        row {
                            textFieldWithBrowseButton(
                                "Specify path to go-arch-lint binary",
                                project,
                                FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor(),
                            )
                                .align(Align.FILL)
                                .text(storageState.executorHostVerifiedBinaryPath)
                                .onReset {
                                    this.text(storageState.executorHostVerifiedBinaryPath)
                                }
                                .onChanged {
                                    val path = it.textField.text
                                    if (path === null) return@onChanged

                                    if (path == storageState.executorHostVerifiedBinaryPath) {
                                        return@onChanged
                                    }

                                    // drop last state
                                    asyncModified = true
                                    uiLinterHostVerifyFailRow.visible(true)
                                    uiLinterHostVerifySuccessRow.visible(false)
                                    uiLinterHostVerifyVersion.text("?")

                                    // verify linter and update ui
                                    storage.chooseLinterHostPath(path) {
                                        uiLinterHostVerifyFailRow.visible(false)
                                        uiLinterHostVerifySuccessRow.visible(true)
                                        uiLinterHostVerifyVersion.text("Linter Version: ${storageState.executorHostVerifiedVersion}")
                                    }
                                }
                        }

                        uiLinterHostVerifySuccessRow = row {
                            icon(GoArchIcons.FILETYPE_ICON)
                            uiLinterHostVerifyVersion =
                                text("Linter Version: ${storageState.executorHostVerifiedVersion}")
                                    .bold()
                        }.visible(storageState.executorHostVerifiedVersion.valid)

                        uiLinterHostVerifyFailRow = row {
                            icon(AllIcons.General.Warning)
                            text("No linter is used right now")
                        }.visible(!storageState.executorHostVerifiedVersion.valid)
                    }

                    // ---------------------------
                    // HOST: Setup
                    // ---------------------------

                    group("Setup") {
                        row {
                            button("Copy") {
                                CopyPasteManager.getInstance().setContents(
                                    StringSelection(
                                        "go install github.com/fe3dback/go-arch-lint@latest"
                                    )
                                )
                            }
                            textField()
                                .text("go install github.com/fe3dback/go-arch-lint@latest")
                                .align(Align.FILL)
                                .enabled(false)
                        }
                        row {
                            browserLink(
                                "or download from project page",
                                "https://github.com/fe3dback/go-arch-lint/releases"
                            )
                        }
                        row {
                            comment("Currently only manual install is possible")
                        }
                    }

                }.visibleIf(uiEnableIntegrations.selected)

                // ---------------------------
                // Integrations
                // ---------------------------

                group("Active integrations") {
                    row {
                        checkBox("Validate config").bindSelected(storageState::enableSubSelfInspections)
                    }
                    row {
                        checkBox("Lint code").enabled(false)
                        comment("(todo)")
                    }
                    row {
                        checkBox("Show component mappings on packages").enabled(false)
                        comment("(todo)")
                    }
                }.visibleIf(uiEnableIntegrations.selected)

            }.visible(Features.isExecutorAvailable())

            group {
                row {
                    comment("Currently advanced linter integration supported only on Unix like OS")
                }
            }.visible(!Features.isExecutorAvailable())
        }
    }

    override fun getId(): String = "com.github.fe3dback.go-arch-lint.settings"

    override fun isModified(): Boolean {
        if (asyncModified) {
            asyncModified = false
            return true
        }

        if (super<BoundConfigurable>.isModified()) return true
        return project.goArchLintStorage.state != storageState
    }

    override fun apply() {
        super.apply()
        project.goArchLintStorage.state = storageState
    }

}