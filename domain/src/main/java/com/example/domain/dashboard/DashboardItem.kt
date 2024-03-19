package com.example.domain.dashboard

interface DashboardItem {

    interface Mapper<T : Any> {

        fun map(fromCurrency: String, toCurrency: String, rates: Double): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T

    data class Base(
        private val fromCurrency: String,
        private val toCurrency: String,
        private val rates: Double
    ) : DashboardItem {

        override fun <T : Any> map(mapper: Mapper<T>) = mapper.map(fromCurrency, toCurrency, rates)
    }
}