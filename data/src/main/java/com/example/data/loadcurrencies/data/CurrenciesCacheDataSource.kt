package com.example.data.loadcurrencies.data

import android.content.Context
import androidx.room.Room

interface CurrenciesCacheDataSource {

    suspend fun currencies(): List<String>

    suspend fun saveCurrencies(currencies: List<String>)

    class Base(private val dao: CurrenciesDao) : CurrenciesCacheDataSource {

        constructor(context: Context) : this(
            Room.databaseBuilder(
                context,
                CurrenciesDatabase::class.java,
                "currencies_db"
            ).build().currenciesDao()
        )


        override suspend fun currencies(): List<String> {
            return dao.currencies().map { it.currency }
        }

        override suspend fun saveCurrencies(currencies: List<String>) {
            dao.saveCurrencies(currencies.map { CurrencyEntity(it) })
        }
    }
}