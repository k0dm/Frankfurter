package com.example.domain.dashboard

interface DashboardRepository {

    suspend fun dashboards(): DashboardResult

    suspend fun removePair(from: String, to: String): DashboardResult
}