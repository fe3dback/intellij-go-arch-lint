package com.github.fe3dback.intellijgoarchlint.settings

import com.github.fe3dback.intellijgoarchlint.cmp.integration.installer.ArchLintVerifier
import com.github.fe3dback.intellijgoarchlint.cmp.notifications.CommonNotification
import com.github.fe3dback.intellijgoarchlint.models.invalidVersionFrom
import com.intellij.configurationStore.serializeObjectInto
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer.deserializeInto
import org.jdom.Element
import com.intellij.openapi.components.State as StateComponent
import com.intellij.openapi.components.Storage as StorageComponent

private const val elementID: String = "GoArchLintSettings"

val Project.goArchLintStorage: Storage get() = service()

@Service
@StateComponent(name = elementID, storages = [StorageComponent(StoragePathMacros.WORKSPACE_FILE)])
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

    // todo: verifyHostBinary called before params saved in form
    fun verifyHostBinary() {
        // clear verify flag
        state.executorHostVerified = false
        state.executorHostVerifiedVersion = invalidVersionFrom("?")

        // verify linter
        val version = try {
            ArchLintVerifier.verifyBinary(project, state.executorHostTmpBinaryPath)
        } catch (e: Exception) {
            CommonNotification.show(project, "verify failed: ${e.message}", NotificationType.ERROR)
            return
        }

        // check version
        if (!version.valid) {
            CommonNotification.show(project, "unable to verify: unknown version: $version", NotificationType.WARNING)
            return
        }

        // verify
        state.executorHostVerified = true
        state.executorHostVerifiedVersion = version
        state.executorHostVerifiedBinaryPath = state.executorHostTmpBinaryPath
    }
}
