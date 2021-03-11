package com.github.fe3dback.intellijgoarchlint.reference.contributor

import com.github.fe3dback.intellijgoarchlint.reference.GoArchComponentNameReferenceProvider
import com.github.fe3dback.intellijgoarchlint.reference.GoArchVendorNameReferenceProvider
import com.github.fe3dback.intellijgoarchlint.reference.pattern.GoArchPsiPattern
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class GoArchReferenceContributor: PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // commonComponents.$item -> components
        registrar.registerReferenceProvider(
            GoArchPsiPattern.commonComponents(),
            GoArchComponentNameReferenceProvider()
        )

        // commonVendors.$item -> vendors
        registrar.registerReferenceProvider(
            GoArchPsiPattern.commonVendors(),
            GoArchVendorNameReferenceProvider()
        )

        // deps.$name -> components
        registrar.registerReferenceProvider(
            GoArchPsiPattern.componentDependencies(),
            GoArchComponentNameReferenceProvider()
        )

        // deps.%.mayDependOn.$item -> components
        registrar.registerReferenceProvider(
            GoArchPsiPattern.mayDependInDependencies(),
            GoArchComponentNameReferenceProvider()
        )

        // deps.%.canUse.$item -> vendors
        registrar.registerReferenceProvider(
            GoArchPsiPattern.canUseInDependencies(),
            GoArchVendorNameReferenceProvider()
        )
    }
}