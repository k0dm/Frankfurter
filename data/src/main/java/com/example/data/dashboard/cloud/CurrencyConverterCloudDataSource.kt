package com.example.data.dashboard.cloud

interface CurrencyConverterCloudDataSource {

    suspend fun exchangeRate(from: String, to: String): Double

    class Base(private val service: CurrencyConverterService) : CurrencyConverterCloudDataSource {

        override suspend fun exchangeRate(from: String, to: String): Double =
            service.exchangeRate(from, to).execute().body()!!.rate(to)
    }
}