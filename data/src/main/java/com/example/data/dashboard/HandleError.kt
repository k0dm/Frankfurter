package com.example.data.dashboard

import com.example.data.core.ProvideResources
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

interface HandleError {

    fun handle(e: Exception): String

    @Singleton
    class Base @Inject constructor(
        private val provideResources: ProvideResources
    ) : HandleError {

        override fun handle(e: Exception) = with(provideResources) {
            return@with if (e is UnknownHostException) {
                noInternetConnectionMessage()
            } else {
                serviceUnavailableMessage()
            }
        }
    }
}