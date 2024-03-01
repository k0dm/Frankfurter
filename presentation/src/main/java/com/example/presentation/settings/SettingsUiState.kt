package com.example.presentation.settings

import com.example.presentation.core.UpdateAdapter

interface SettingsUiState {

    fun show(
        currencyFromAdapter: UpdateAdapter<CurrencyUi>,
        currencyToAdapter: UpdateAdapter<CurrencyUi>
    )

    object Empty : SettingsUiState {

        override fun show(
            currencyFromAdapter: UpdateAdapter<CurrencyUi>,
            currencyToAdapter: UpdateAdapter<CurrencyUi>
        ) = Unit
    }

    data class Initial(private val fromCurrencies: List<CurrencyUi>) : SettingsUiState {

        override fun show(
            currencyFromAdapter: UpdateAdapter<CurrencyUi>,
            currencyToAdapter: UpdateAdapter<CurrencyUi>
        ) {
            currencyFromAdapter.update(fromCurrencies)
        }
    }

    data class AvailableDestinations(
        private val fromCurrencies: List<CurrencyUi>,
        private val toCurrencies: List<CurrencyUi>
    ) : SettingsUiState {

        override fun show(
            currencyFromAdapter: UpdateAdapter<CurrencyUi>,
            currencyToAdapter: UpdateAdapter<CurrencyUi>
        ) {
            currencyFromAdapter.update(fromCurrencies)
            currencyToAdapter.update(toCurrencies)
        }
    }

    data class ReadyToSave(private val toCurrencies: List<CurrencyUi>) : SettingsUiState {

        override fun show(
            currencyFromAdapter: UpdateAdapter<CurrencyUi>,
            currencyToAdapter: UpdateAdapter<CurrencyUi>
        ) {
            currencyToAdapter.update(toCurrencies)
        }
    }
}