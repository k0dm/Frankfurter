package com.example.data.dashboard.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteCurrenciesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(currencyPair: CurrencyPairEntity)

    @Query("SELECT * FROM favorite_currencies")
    suspend fun favoriteCurrencies(): List<CurrencyPairEntity>
}

