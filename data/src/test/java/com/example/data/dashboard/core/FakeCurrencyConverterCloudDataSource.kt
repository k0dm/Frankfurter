package com.example.data.dashboard.core

import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource

internal class FakeCurrencyConverterCloudDataSource : CurrencyConverterCloudDataSource {

    override suspend fun exchangeRate(from: String, to: String): Double {
        return 1.1
    }
}