package com.github.fe3dback.intellijgoarchlint.integration.sdk

import com.github.fe3dback.intellijgoarchlint.integration.GoArchSDK
import com.github.fe3dback.intellijgoarchlint.integration.executor.HostExecutor

class Provider {
    val archSDK: GoArchSDK = provideSDK()

    private fun provideSDK(): GoArchSDK {
        return SDK(
            HostExecutor()
        )
    }
}