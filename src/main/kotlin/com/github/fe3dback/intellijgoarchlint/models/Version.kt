package com.github.fe3dback.intellijgoarchlint.models

class Version(
    val valid: Boolean,
    val devel: Boolean,
    val raw: String,
    val major: Int,
    val minor: Int,
    val patch: Int,
) {
    override fun toString(): String {
        if (!valid && raw.isNotEmpty()) {
            return "unknown (${raw})"
        }

        if (!valid) {
            return "unknown"
        }

        if (devel) {
            return raw
        }

        return "v${major}.${minor}.${patch}"
    }
}

fun invalidVersionFrom(version: String): Version = Version(
    valid = false,
    devel = false,
    raw = version,
    major = 0,
    minor = 0,
    patch = 0
)

// examples: "v1.11.0", "1.10.0", "(devel)"
fun parseRawVersion(raw: String): Version {
    // not semver
    if (raw.contains("dev")) {
        return Version(valid = true, devel = true, raw = raw, major = 0, minor = 0, patch = 0)
    }

    // semver
    var version = raw

    if (version.startsWith("v")) {
        version = version.removePrefix("v")
    }

    val parts = version.split(".")
    if (parts.count() != 3) {
        return invalidVersionFrom(version)
    }

    val (major, minor, patch) = parts

    return try {
        Version(
            valid = true,
            devel = false,
            raw = version,
            major = major.toInt(),
            minor = minor.toInt(),
            patch = patch.toInt()
        )
    } catch (e: NumberFormatException) {
        invalidVersionFrom(version)
    }
}