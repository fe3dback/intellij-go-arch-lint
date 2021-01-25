package com.github.fe3dback.intellijgoarchlint.reference.element

import com.github.fe3dback.intellijgoarchlint.file.GoArchFileType
import com.github.fe3dback.intellijgoarchlint.file.GoArchIcons
import com.intellij.find.actions.ShowUsagesAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.meta.PsiMetaData
import com.intellij.psi.meta.PsiMetaOwner
import com.intellij.psi.meta.PsiPresentableMetaData
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import javax.swing.Icon

class GoArchSectionPsiElement(
    private val parent: PsiElement,
    private val typeName: String,
    private val elementName: String,
): FakePsiElement(), PsiMetaOwner, PsiPresentableMetaData {
    override fun getDeclaration(): PsiElement = parent
    override fun getNavigationElement(): PsiElement = parent
    override fun getName(): String = ElementManipulators.getValueText(parent)
    override fun getName(context: PsiElement?): String = name
    override fun getParent(): PsiElement = parent
    override fun getIcon(): Icon = GoArchIcons.FILETYPE_ICON
    override fun init(element: PsiElement?) {}
    override fun getMetaData(): PsiMetaData = this
    override fun getTypeName(): String = typeName

    override fun getResolveScope(): GlobalSearchScope {
        return GlobalSearchScope
            .getScopeRestrictedByFileTypes(
                GlobalSearchScope.allScope(parent.project),
                GoArchFileType.INSTANCE
            )
    }

    override fun getUseScope(): SearchScope = resolveScope

    override fun isEquivalentTo(another: PsiElement?): Boolean {
        return equals(another) ||
            (
                another != null
                && another is GoArchSectionPsiElement
                && another.elementName == elementName
                && another.typeName == typeName
            )
    }

    override fun navigate(requestFocus: Boolean) {
        if (DumbService.getInstance(project).isDumb) return
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        val popupPosition = JBPopupFactory.getInstance().guessBestPopupLocation(editor)

        ShowUsagesAction.startFindUsages(this, popupPosition, editor)
    }
}