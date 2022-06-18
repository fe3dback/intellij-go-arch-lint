package com.github.fe3dback.intellijgoarchlint.reference.regexp

import com.github.fe3dback.intellijgoarchlint.file.GoArchFileType
import com.github.fe3dback.intellijgoarchlint.reference.pattern.GoArchPsiPattern
import com.intellij.lang.injection.general.Injection
import com.intellij.lang.injection.general.LanguageInjectionContributor
import com.intellij.lang.injection.general.SimpleInjection
import com.intellij.psi.PsiElement
import org.intellij.lang.regexp.RegExpLanguage

class GoArchRegexpInjector : LanguageInjectionContributor {
    // called for each yaml element
    override fun getInjection(context: PsiElement): Injection? {
        val virtualFile = context.containingFile.virtualFile ?: return null

        // yaml element, not inside goArch config
        if (!GoArchFileType.INSTANCE.isMyFileType(virtualFile)) {
            return null
        }

        // element not inside "excluded files" section
        if (!GoArchPsiPattern.excludeFiles().accepts(context)) {
            return null
        }

        return SimpleInjection(RegExpLanguage.INSTANCE, "", "", null)
    }
}