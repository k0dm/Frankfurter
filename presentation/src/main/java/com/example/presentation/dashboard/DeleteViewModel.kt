package com.example.presentation.dashboard

import android.util.Log
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteViewModel @Inject constructor(
    private val repository: DashboardRepository,
    runAsync: RunAsync,
    private val mapper: DashboardResult.Mapper
) : BaseViewModel(runAsync) {

    init {
        Log.d("k0dm", "DeleteViewModel ${hashCode()}")
    }

    fun removePair(from: String, to: String) = runAsync({
        repository.removePair(from, to)
    }) { dashboardResult ->
        dashboardResult.map(mapper)
    }
}