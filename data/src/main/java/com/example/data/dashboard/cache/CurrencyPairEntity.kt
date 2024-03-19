package com.example.data.dashboard.cache

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "favorite_currencies", primaryKeys = ["from", "to"])
data class CurrencyPairEntity(
    @ColumnInfo("from")
    val fromCurrency: String,
    @ColumnInfo("to")
    val toCurrency: String,
    @ColumnInfo("rates")
    val rates: Double = -1.0,
    @ColumnInfo("data")
    val date: String = ""
) {

    fun isInvalidRate(currentDate: CurrentDate): Boolean {
        return rates == -1.0 || date != currentDate.date()
    }
}