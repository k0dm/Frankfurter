package com.example.presentation.dashboard

import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi
import com.example.presentation.dashboard.adapter.UpdateAdapter

interface DashboardUiState {

    fun show(adapter: UpdateAdapter)

    object Progress : DashboardUiState {

        override fun show(adapter: UpdateAdapter) =
            adapter.update(listOf(DashboardCurrencyPairUi.Progress))
    }

    object Empty : DashboardUiState {

        override fun show(adapter: UpdateAdapter) =
            adapter.update(listOf(DashboardCurrencyPairUi.Empty))
    }

    data class Error(private val message: String) : DashboardUiState {

        override fun show(adapter: UpdateAdapter) =
            adapter.update(listOf(DashboardCurrencyPairUi.Error(message)))
    }

    data class Success(private val currencies: List<DashboardCurrencyPairUi>) : DashboardUiState {

        override fun show(adapter: UpdateAdapter) = adapter.update(currencies)
    }
}