package com.example.data.dashboard

import com.example.data.core.ProvideResources
import java.net.UnknownHostException

interface HandleError {

    fun handle(e: Exception): String

    class Base(private val provideResources: ProvideResources) : HandleError {

        override fun handle(e: Exception) = with(provideResources) {
            return@with if (e is UnknownHostException) {
                noInternetConnectionMessage()
            } else {
                serviceUnavailableMessage()
            }
        }
    }
}