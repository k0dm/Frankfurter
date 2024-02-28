package com.example.data.dashboard.cache

interface FavoriteCurrenciesCacheDataSource {

    interface Read {

        suspend fun favoriteCurrencies(): List<CurrencyPairEntity>
    }

    interface Save {

        suspend fun save(currencyPair: CurrencyPairEntity)
    }

    interface Mutable : Read, Save

    class Base(private val dao: FavoriteCurrenciesDao) : Mutable {

        override suspend fun favoriteCurrencies() = dao.favoriteCurrencies()

        override suspend fun save(currencyPair: CurrencyPairEntity) {
            dao.save(currencyPair)
        }
    }
}