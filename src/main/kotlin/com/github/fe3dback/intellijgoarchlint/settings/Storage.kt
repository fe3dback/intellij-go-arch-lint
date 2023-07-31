package com.github.fe3dback.intellijgoarchlint.settings

import com.github.fe3dback.intellijgoarchlint.cmp.integration.installer.ArchLintVerifier
import com.github.fe3dback.intellijgoarchlint.models.Version
import com.github.fe3dback.intellijgoarchlint.models.invalidVersionFrom
import com.intellij.configurationStore.serializeObjectInto
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer.deserializeInto
import org.jdom.Element
import java.io.File
import java.util.concurrent.Callable
import com.intellij.openapi.components.State as StateComponent
import com.intellij.openapi.components.Storage as StorageComponent

private const val elementID: String = "GoArchLintSettings"

val Project.goArchLintStorage: Storage get() = service()

@Service(Service.Level.PROJECT)
@StateComponent(name = elementID, storages = [StorageComponent("go-arch-lint.xml")])
class Storage(private val project: Project) :
    PersistentStateComponent<Element> {

    var state: State = State()

    override fun getState(): Element {
        val src = state.copy()
        val dst = Element(elementID)
        serializeObjectInto(src, dst)
        return dst
    }

    override fun loadState(src: Element) = deserializeInto(state, src.clone())

    override fun noStateLoaded() {
        super.noStateLoaded()
        loadState(getState())
    }

    override fun initializeComponent() {
        super.initializeComponent()

        // load linter version
        if (state.executorHostVerifiedBinaryPath != "") {
            chooseLinterHostPath(state.executorHostVerifiedBinaryPath) { }
        }
    }

    fun chooseLinterHostPath(path: String, onSuccess: (Version) -> Unit) {
        state.executorHostVerifiedVersion = invalidVersionFrom("?")
        state.executorHostVerifiedBinaryPath = ""

        // check linter and setup it again
        ApplicationManager.getApplication().executeOnPooledThread(Callable {
            if (!File(path).canExecute()) return@Callable

            val version = ArchLintVerifier.verifyBinary(project, path)
            if (!version.valid) {
                return@Callable
            }

            state.executorHostVerifiedVersion = version
            state.executorHostVerifiedBinaryPath = path
            onSuccess(version)
        })
    }
}
