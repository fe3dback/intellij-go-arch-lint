package com.github.fe3dback.intellijgoarchlint.settings

import com.intellij.configurationStore.serializeObjectInto
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
}
