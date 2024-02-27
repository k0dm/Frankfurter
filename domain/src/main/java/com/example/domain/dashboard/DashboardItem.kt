package com.example.domain.dashboard

interface DashboardItem {

    interface Mapper<T : Any> {

        fun map(fromCurrency: String, toCurrency: String, rates: String): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T

    data class Base(
        private val fromCurrency: String,
        private val toCurrency: String,
        private val rates: String
    ) : DashboardItem {

        override fun <T : Any> map(mapper: Mapper<T>) = mapper.map(fromCurrency, toCurrency, rates)
    }
}