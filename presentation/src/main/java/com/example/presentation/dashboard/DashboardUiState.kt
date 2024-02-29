package com.example.presentation.dashboard

import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi
import com.example.presentation.dashboard.adapter.UpdateAdapter

interface DashboardUiState {

    fun show(adapter: UpdateAdapter)

    abstract class Abstract(
        private val dashboardCurrencyPairUi: DashboardCurrencyPairUi
    ) : DashboardUiState {
        override fun show(adapter: UpdateAdapter) = adapter.update(listOf(dashboardCurrencyPairUi))
    }

    object Progress : Abstract(DashboardCurrencyPairUi.Progress)

    object Empty : Abstract(DashboardCurrencyPairUi.Empty)

    data class Error(private val message: String) : Abstract(DashboardCurrencyPairUi.Error(message))

    data class Success(private val currencies: List<DashboardCurrencyPairUi>) : DashboardUiState {

        override fun show(adapter: UpdateAdapter) = adapter.update(currencies)
    }
}