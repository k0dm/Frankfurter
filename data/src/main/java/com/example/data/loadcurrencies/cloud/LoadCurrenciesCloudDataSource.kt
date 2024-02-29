package com.example.data.loadcurrencies.cloud

interface LoadCurrenciesCloudDataSource {

    fun currencies(): List<String>

    class Base(private val currenciesService: CurrenciesService) : LoadCurrenciesCloudDataSource {

        override fun currencies() = currenciesService.currencies().execute().body()!!.keys.toList()
    }
}