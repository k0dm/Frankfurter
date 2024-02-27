package com.example.data.dashboard.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "favorite_currencies", primaryKeys = ["fromCurrency", "toCurrency"])
data class CurrencyPairEntity(
    @ColumnInfo("from")
    val fromCurrency: String,
    @ColumnInfo("to")
    val toCurrency: String,
    @ColumnInfo("rates")
    val rates: Double = -1.0,
    @ColumnInfo("data")
    val date: String = DateFormatter.date()
) {

    fun isInvalidRate(): Boolean {
        return rates == -1.0 || date != DateFormatter.date()
    }
}

object DateFormatter {

    fun date(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
}