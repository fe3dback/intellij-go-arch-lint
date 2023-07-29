package com.github.fe3dback.intellijgoarchlint.cmp.integration.sdk

import com.github.fe3dback.intellijgoarchlint.cmp.integration.GoArchSDK
import com.github.fe3dback.intellijgoarchlint.cmp.integration.executor.Executor
import com.github.fe3dback.intellijgoarchlint.cmp.integration.executor.ExecutorHost
import com.github.fe3dback.intellijgoarchlint.cmp.integration.executor.ExecutorProcessIndicatorDecorator
import com.github.fe3dback.intellijgoarchlint.cmp.integration.executor.ExecutorTrustDecorator

class SDKProvider {
    companion object {
        private var archSDK: GoArchSDK? = null

        fun goArchLintSDK(): GoArchSDK {
            if (archSDK !== null) {
                return archSDK!!
            }

            archSDK = provideSDK()
            return archSDK!!
        }

        private fun provideSDK(): GoArchSDK {
            return SDK(
                ExecutorTrustDecorator(
                    Executor(
                        ExecutorProcessIndicatorDecorator(ExecutorHost()),
                        ExecutorProcessIndicatorDecorator(ExecutorHost()), // todo: docker
                    )
                )
            )
        }
    }
}