package com.example.presentation.dashboard

import com.example.presentation.core.UpdateAdapter
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi

interface DashboardUiState {

    fun show(adapter: UpdateAdapter<DashboardCurrencyPairUi>)

    abstract class Abstract(
        private val dashboardCurrencyPairUi: DashboardCurrencyPairUi
    ) : DashboardUiState {
        override fun show(adapter: UpdateAdapter<DashboardCurrencyPairUi>) =
            adapter.update(listOf(dashboardCurrencyPairUi))
    }

    object Progress : Abstract(DashboardCurrencyPairUi.Progress)

    object Empty : Abstract(DashboardCurrencyPairUi.Empty)

    data class Error(private val message: String) : Abstract(DashboardCurrencyPairUi.Error(message))

    data class Success(private val currencies: List<DashboardCurrencyPairUi>) : DashboardUiState {

        override fun show(adapter: UpdateAdapter<DashboardCurrencyPairUi>) =
            adapter.update(currencies)
    }
}