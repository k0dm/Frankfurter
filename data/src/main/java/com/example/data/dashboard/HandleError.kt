package com.example.data.dashboard

import com.example.data.core.ProvideResources
import java.net.UnknownHostException

interface HandleError {

    fun handle(e: Exception): String

    class Base(private val provideResources: ProvideResources) : HandleError {

        override fun handle(e: Exception) = if (e is UnknownHostException) {
            provideResources.noInternetConnectionMessage()
        } else {
            provideResources.serviceUnavailableMessage()
        }
    }
}