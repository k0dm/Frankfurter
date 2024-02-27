package com.example.domain.dashboard

interface DashBoardResult {

    interface Mapper {

        fun mapSuccess(listOfItems: List<DashboardItem>)

        fun mapError(message: String)
    }

    fun map(mapper: Mapper)

    object Empty : DashBoardResult {
        override fun map(mapper: Mapper) = Unit
    }

    data class Error(private val message: String) : DashBoardResult {

        override fun map(mapper: Mapper) {
            mapper.mapError(message)
        }
    }

    data class Success(private val listOfItems: List<DashboardItem>) : DashBoardResult {

        override fun map(mapper: Mapper) {
            mapper.mapSuccess(listOfItems)
        }
    }
}