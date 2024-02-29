package com.example.presentation.dashboard

import java.text.DecimalFormat

interface RatesFormatter {

    fun format(rates: Double): String

    class Base(pattern: String = "#.##") : RatesFormatter {

        private val decimalFormat = DecimalFormat(pattern)

        override fun format(rates: Double): String = decimalFormat.format(rates)
    }
}