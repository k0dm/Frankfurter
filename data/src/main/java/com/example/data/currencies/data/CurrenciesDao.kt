package com.example.data.currencies.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrenciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrencies(currency: CurrencyEntity)

    @Query("SELECT * FROM currencies")
    suspend fun currencies(): List<CurrencyEntity>
}