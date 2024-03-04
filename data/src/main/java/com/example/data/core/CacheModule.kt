package com.example.data.core

import android.content.Context
import androidx.room.Room
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource

interface CacheModule {

    fun favoriteCurrenciesCacheDataSource(): FavoriteCurrenciesCacheDataSource.Mutable

    fun currenciesCacheDataSource(): CurrenciesCacheDataSource.Mutable

    class Base(context: Context) : CacheModule {

        private val db = Room.databaseBuilder(
            context,
            CurrenciesDatabase::class.java,
            "currencies_db"
        ).build()
        private val favoriteCurrenciesCacheDataSource = FavoriteCurrenciesCacheDataSource.Base(
            db.favoriteCurrenciesDao()
        )
        private val currenciesCacheDataSource = CurrenciesCacheDataSource.Base(
            db.currenciesDao()
        )

        override fun favoriteCurrenciesCacheDataSource() = favoriteCurrenciesCacheDataSource

        override fun currenciesCacheDataSource() = currenciesCacheDataSource
    }
}