package com.github.fe3dback.intellijgoarchlint.settings

import com.intellij.configurationStore.serializeObjectInto
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer.deserializeInto
import org.jdom.Element
import com.intellij.openapi.components.Storage as StorageComponent

private const val elementID: String = "GoArchLintSettings"

val Project.goArchLintStorage: Storage get() = service()

@Service
@State(name = elementID, storages = [StorageComponent(StoragePathMacros.WORKSPACE_FILE)])
class Storage(private val project: Project) :
    PersistentStateComponent<Element> {

    data class State(
        var testStringField: String = "",
    )

    var state: State = State()

    override fun getState(): Element {
        val src = state.copy()
        val dst = Element(elementID)
        serializeObjectInto(src, dst)
        return dst
    }

    override fun loadState(src: Element) = deserializeInto(state, src.clone())
}
