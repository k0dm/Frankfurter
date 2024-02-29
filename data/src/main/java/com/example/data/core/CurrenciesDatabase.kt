package com.example.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesDao
import com.example.data.loadcurrencies.cache.CurrenciesDao
import com.example.data.loadcurrencies.cache.CurrencyEntity

@Database(
    version = 1,
    exportSchema = true,
    entities = [CurrencyEntity::class, CurrencyPairEntity::class],
)
abstract class CurrenciesDatabase : RoomDatabase() {

    abstract fun currenciesDao(): CurrenciesDao

    abstract fun favoriteCurrenciesDao(): FavoriteCurrenciesDao
}