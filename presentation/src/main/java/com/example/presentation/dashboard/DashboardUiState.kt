package com.example.presentation.dashboard

import com.example.presentation.dashboard.adapter.DashboardAdapter
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi

interface DashboardUiState {

    fun show(adapter: DashboardAdapter)

    object Progress : DashboardUiState {

        override fun show(adapter: DashboardAdapter) =
            adapter.update(listOf(DashboardCurrencyPairUi.Progress))
    }

    object Empty : DashboardUiState {

        override fun show(adapter: DashboardAdapter) =
            adapter.update(listOf(DashboardCurrencyPairUi.Empty))
    }

    data class Error(private val message: String) : DashboardUiState {

        override fun show(adapter: DashboardAdapter) =
            adapter.update(listOf(DashboardCurrencyPairUi.Error(message)))
    }

    data class Success(private val currencies: List<DashboardCurrencyPairUi>) : DashboardUiState {

        override fun show(adapter: DashboardAdapter) = adapter.update(currencies)
    }
}