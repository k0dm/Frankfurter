package com.example.data.currencies.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = true, entities = [CurrencyEntity::class])
abstract class CurrenciesDatabase : RoomDatabase() {

    abstract fun currenciesDao(): CurrenciesDao
}