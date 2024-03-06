package com.example.domain.dashboard

import kotlinx.coroutines.CoroutineScope


interface DashboardRepository {

    suspend fun dashboards(viewModelScope: CoroutineScope): DashboardResult

    suspend fun removePair(
        from: String,
        to: String,
        viewModelScope: CoroutineScope
    ): DashboardResult
}