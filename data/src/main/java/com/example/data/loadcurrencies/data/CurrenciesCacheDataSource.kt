package com.example.data.loadcurrencies.data

interface CurrenciesCacheDataSource {

    suspend fun currencies(): List<String>

    suspend fun saveCurrencies(currencies: List<String>)

    class Base(private val dao: CurrenciesDao) : CurrenciesCacheDataSource {

        override suspend fun currencies(): List<String> {
            return dao.currencies().map { it.currency }
        }

        override suspend fun saveCurrencies(currencies: List<String>) {
            currencies.forEach {
                dao.saveCurrencies(CurrencyEntity(it))
            }
        }
    }
}