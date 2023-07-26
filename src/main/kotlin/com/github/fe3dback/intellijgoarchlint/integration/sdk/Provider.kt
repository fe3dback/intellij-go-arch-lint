package com.github.fe3dback.intellijgoarchlint.integration.sdk

import com.github.fe3dback.intellijgoarchlint.integration.GoArchSDK

class Provider {
    val LinterSDK: GoArchSDK = provideSDK()

    private fun provideSDK(): GoArchSDK {
        return SDK()
    }
}