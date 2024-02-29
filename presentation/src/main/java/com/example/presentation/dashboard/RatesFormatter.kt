package com.example.presentation.dashboard

import java.text.DecimalFormat

interface RatesFormatter {

    fun format(rates: Double): String

    class Base(
        private val pattern: String = "#.##"
    ) : RatesFormatter {

        override fun format(rates: Double): String = DecimalFormat(pattern).format(rates)
    }
}