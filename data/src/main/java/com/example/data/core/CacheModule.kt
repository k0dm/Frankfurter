package com.example.data.core

import android.content.Context
import androidx.room.Room

interface CacheModule {

    fun database(): CurrenciesDatabase

    class Base(context: Context) : CacheModule {

        private val db = Room.databaseBuilder(
            context,
            CurrenciesDatabase::class.java,
            "currencies_db"
        ).build()

        override fun database() = db
    }
}