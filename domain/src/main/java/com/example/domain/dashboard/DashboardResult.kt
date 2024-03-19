package com.example.domain.dashboard

interface DashboardResult {

    interface Mapper {

        fun mapSuccess(listOfItems: List<DashboardItem>)

        fun mapError(message: String)

        fun mapEmpty()
    }

    fun map(mapper: Mapper)

    object Empty : DashboardResult {
        override fun map(mapper: Mapper) {
            mapper.mapEmpty()
        }
    }

    data class Error(private val message: String) : DashboardResult {

        override fun map(mapper: Mapper) {
            mapper.mapError(message)
        }
    }

    data class Success(private val listOfItems: List<DashboardItem>) : DashboardResult {

        override fun map(mapper: Mapper) {
            mapper.mapSuccess(listOfItems)
        }
    }

    object NoDataYet : DashboardResult {
        override fun map(mapper: Mapper) = Unit
    }
}