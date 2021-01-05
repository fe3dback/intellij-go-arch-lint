package com.github.fe3dback.intellijgoarchlint.project

import com.github.fe3dback.intellijgoarchlint.file.GoArchFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.annotations.NotNull

object GoArchProjectUtils {
    fun findGoArchFilesInProject(project: Project): @NotNull MutableCollection<VirtualFile> {
        return FileTypeIndex.getFiles(GoArchFileType.INSTANCE, GlobalSearchScope.allScope(project))
    }
}