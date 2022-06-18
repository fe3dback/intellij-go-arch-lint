package com.github.fe3dback.intellijgoarchlint.exec

data class DockerRunParams(
    val imageName: String = "",
    val imageTag: String = "",
    val removeAfterExec: Boolean = true,
    val mountVolume: String,
    val command: String
)

class DockerParamsFactory() {
    private var imageName: String = ""
    private var imageTag: String = ""
    private var removeAfterExec: Boolean = true
    private var mountVolume: String = ""
    private var command: String = ""

    fun withImage(name: String, tag: String) = apply {
        this.imageName = name
        this.imageTag = tag
    }

    fun withMount(from: String, to: String) = apply {
        this.mountVolume = "${from}:${to}"
    }

    fun withAutoRemove(remove: Boolean) = apply {
        this.removeAfterExec = remove
    }

    fun withCommand(command: String) = apply {
        this.command = command
    }

    fun build(): DockerRunParams {
        return DockerRunParams(imageName, imageTag, removeAfterExec, mountVolume, command)
    }
}