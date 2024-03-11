package com.example.data.loadcurrencies.cloud

import javax.inject.Inject
import javax.inject.Singleton

interface LoadCurrenciesCloudDataSource {

    fun currencies(): List<String>

    @Singleton
    class Base @Inject constructor(private val currenciesService: CurrenciesService) :
        LoadCurrenciesCloudDataSource {

        override fun currencies() = currenciesService.currencies().execute().body()!!.keys.toList()
    }
}