package com.github.fe3dback.intellijgoarchlint

object GoArch {
    // top level
    const val specDeps = "deps"
    const val specVersion = "version"
    const val specAllow = "allow"
    const val specExclude = "exclude"
    const val specExcludeFiles = "excludeFiles"
    const val specComponents = "components"
    const val specVendors = "vendors"
    const val specCommonComponents = "commonComponents"
    const val specCommonVendors = "commonVendors"

    // in deps
    const val specDepsMayDependOn = "mayDependOn"
    const val specDepsCanUse = "canUse"
    const val specDepsAnyVendorDeps = "anyVendorDeps"
    const val specDepsAnyProjectDeps = "anyProjectDeps"
}