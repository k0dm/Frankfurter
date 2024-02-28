package com.example.data.dashboard.cache

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface CurrentDate {

    fun date(): String

    class Base(
        private val date: LocalDateTime = LocalDateTime.now(),
        private val format: String = "yyyy/MM/dd"
    ) : CurrentDate {

        override fun date(): String = date.format(DateTimeFormatter.ofPattern(format))
    }
}