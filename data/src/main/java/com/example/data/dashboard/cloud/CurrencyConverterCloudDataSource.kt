package com.example.data.dashboard.cloud

import javax.inject.Inject

interface CurrencyConverterCloudDataSource {

    suspend fun exchangeRate(from: String, to: String): Double

    class Base @Inject constructor(
        private val service: CurrencyConverterService
    ) : CurrencyConverterCloudDataSource {

        override suspend fun exchangeRate(from: String, to: String) =
            service.exchangeRate(from, to).execute().body()!!.rate(to)
    }
}