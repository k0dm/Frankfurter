package com.example.domain

interface LoadCurrenciesResult {

    fun map(mapper: Mapper)

    interface Mapper {

        fun mapSuccess()

        fun mapError(message: String)
    }

    object Success : LoadCurrenciesResult {

        override fun map(mapper: Mapper) {
            mapper.mapSuccess()
        }
    }

    data class Error(private val message: String) : LoadCurrenciesResult {

        override fun map(mapper: Mapper) {
            mapper.mapError(message)
        }
    }
}