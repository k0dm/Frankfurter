package com.example.data.loadcurrencies.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    @ColumnInfo("currency")
    val currency: String
)