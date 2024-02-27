package com.example.data.core

import android.content.Context
import androidx.room.Room
import com.example.data.loadcurrencies.data.CurrenciesDao
import com.example.data.loadcurrencies.data.CurrenciesDatabase

interface CacheModule {

    fun database(): CurrenciesDatabase

    fun currenciesDao(): CurrenciesDao

    class Base(context: Context) : CacheModule {

        private val db = Room.databaseBuilder(
            context,
            CurrenciesDatabase::class.java,
            "currencies_db"
        ).build()

        override fun database() = db

        override fun currenciesDao() = db.currenciesDao()
    }
}