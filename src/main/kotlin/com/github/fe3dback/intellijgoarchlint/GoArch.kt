package com.github.fe3dback.intellijgoarchlint

const val GoArchLintInstallPath = "~/.config/go-arch-lint/bin/go-arch-lint"

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
    const val specAllowDeepScan = "deepScan" // since v3

    // in deps
    const val specDepsMayDependOn = "mayDependOn"
    const val specDepsCanUse = "canUse"
    const val specDepsAnyVendorDeps = "anyVendorDeps"
    const val specDepsAnyProjectDeps = "anyProjectDeps"
    const val specDepsDeepScan = "deepScan" // since v3
}