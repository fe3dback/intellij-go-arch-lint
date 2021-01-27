package com.github.fe3dback.intellijgoarchlint.reference.contributor

import com.github.fe3dback.intellijgoarchlint.reference.GoArchComponentNameReferenceProvider
import com.github.fe3dback.intellijgoarchlint.reference.GoArchComponentUsagesReferenceProvider
import com.github.fe3dback.intellijgoarchlint.reference.GoArchVendorNameReferenceProvider
import com.github.fe3dback.intellijgoarchlint.reference.pattern.GoArchPsiPattern
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class GoArchReferenceContributor: PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // commonComponents -> components
        registrar.registerReferenceProvider(
            GoArchPsiPattern.commonComponents(),
            GoArchComponentNameReferenceProvider()
        )

        // commonVendors -> vendors
        registrar.registerReferenceProvider(
            GoArchPsiPattern.commonVendors(),
            GoArchVendorNameReferenceProvider()
        )

        // components
        registrar.registerReferenceProvider(
            GoArchPsiPattern.componentNames(),
            GoArchComponentUsagesReferenceProvider()
        )
    }
}