package com.example.domain

interface LoadCurrenciesRepository {

    suspend fun loadCurrencies(): LoadCurrenciesResult
}