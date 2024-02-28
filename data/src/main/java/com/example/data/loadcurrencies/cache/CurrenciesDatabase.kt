package com.example.data.loadcurrencies.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = true, entities = [CurrencyEntity::class])
abstract class CurrenciesDatabase : RoomDatabase() {

    abstract fun currenciesDao(): CurrenciesDao
}