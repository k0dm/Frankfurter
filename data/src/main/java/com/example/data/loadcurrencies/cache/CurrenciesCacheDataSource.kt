package com.example.data.loadcurrencies.cache

interface CurrenciesCacheDataSource {

    interface Read {

        suspend fun currencies(): List<String>
    }

    interface Save {

        suspend fun saveCurrencies(currencies: List<String>)
    }

    interface Mutable : Read, Save

    class Base(private val dao: CurrenciesDao) : Mutable {

        override suspend fun currencies(): List<String> {
            return dao.currencies().map { it.currency }
        }

        override suspend fun saveCurrencies(currencies: List<String>) {
            dao.saveCurrencies(currencies.map { CurrencyEntity(it) })
        }
    }
}

