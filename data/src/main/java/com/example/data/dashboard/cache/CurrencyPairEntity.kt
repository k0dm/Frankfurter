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
    val rates: Double? = null,
    @ColumnInfo("data")
    val date: String = DateFormatter.date()
) {

    fun sameDate(): Boolean = date == DateFormatter.date()
}

object DateFormatter {

    fun date(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
}