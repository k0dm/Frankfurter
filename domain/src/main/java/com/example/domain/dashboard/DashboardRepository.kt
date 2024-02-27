package com.example.domain.dashboard


interface DashboardRepository {

    suspend fun dashboards(): DashBoardResult
}