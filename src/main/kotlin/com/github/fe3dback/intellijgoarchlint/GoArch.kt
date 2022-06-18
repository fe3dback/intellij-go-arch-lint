package com.github.fe3dback.intellijgoarchlint

object GoArch {
    // version range
    // see src/main/resources/schemas
    const val versionMinimum = 1
    const val versionMaximum = 3

    // top level
    const val specVersion = "version"
    const val specDeps = "deps"
    const val specAllow = "allow"
    const val specExclude = "exclude"
    const val specExcludeFiles = "excludeFiles"
    const val specComponents = "components"
    const val specVendors = "vendors"
    const val specCommonComponents = "commonComponents"
    const val specCommonVendors = "commonVendors"

    // in allow
    const val specAllowDepOnAnyVendor = "depOnAnyVendor"

    // in deps
    const val specDepsMayDependOn = "mayDependOn"
    const val specDepsCanUse = "canUse"
    const val specDepsAnyVendorDeps = "anyVendorDeps"
    const val specDepsAnyProjectDeps = "anyProjectDeps"
}