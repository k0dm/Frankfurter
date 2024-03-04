package com.example.data.dashboard.cache

interface FavoriteCurrenciesCacheDataSource {

    interface Read {

        suspend fun favoriteCurrencies(): List<CurrencyPairEntity>
    }

    interface Save {

        suspend fun save(currencyPair: CurrencyPairEntity)
    }

    interface Delete {

        suspend fun delete(currencyPair: CurrencyPairEntity)
    }

    interface ReadAndSave : Read, Save

    interface ReadAndDelete : Read, Delete

    interface Mutable : ReadAndSave, ReadAndDelete

    class Base(private val dao: FavoriteCurrenciesDao) : Mutable {

        override suspend fun favoriteCurrencies() = dao.favoriteCurrencies()

        override suspend fun save(currencyPair: CurrencyPairEntity) = dao.save(currencyPair)

        override suspend fun delete(currencyPair: CurrencyPairEntity) = dao.delete(currencyPair)
    }
}