package com.example.domain.loadcurrencies

interface LoadCurrenciesRepository {

    suspend fun loadCurrencies(): LoadCurrenciesResult
}