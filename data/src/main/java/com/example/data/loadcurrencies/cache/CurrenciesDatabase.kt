package com.example.data.loadcurrencies.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesDao

@Database(
    version = 2,
    exportSchema = true,
    entities = [CurrencyEntity::class, CurrencyPairEntity::class],
)
abstract class CurrenciesDatabase : RoomDatabase() {

    abstract fun currenciesDao(): CurrenciesDao

    abstract fun favoriteCurrenciesDao(): FavoriteCurrenciesDao
}