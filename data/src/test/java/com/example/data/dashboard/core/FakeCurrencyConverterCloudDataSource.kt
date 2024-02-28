package com.example.data.dashboard.core

import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import java.net.UnknownHostException

internal class FakeCurrencyConverterCloudDataSource : CurrencyConverterCloudDataSource {

    private var returnSuccess = true
    private var noInternet = true

    override suspend fun exchangeRate(from: String, to: String): Double {
        return if (returnSuccess) {
            99.9
        } else {
            throw if (noInternet) UnknownHostException() else RuntimeException()
        }
    }

    fun returnFailureNoInternet() {
        returnSuccess = false
        noInternet = true
    }

    fun returnFailureServiceUnavailable() {
        returnSuccess = false
        noInternet = false
    }
}