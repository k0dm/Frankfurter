package com.example.presentation.settings

import com.example.presentation.core.UpdateAdapter
import com.example.presentation.core.views.ChangeVisibility

interface SettingsUiState {

    fun show(
        currencyFromAdapter: UpdateAdapter<CurrencyUi>,
        currencyToAdapter: UpdateAdapter<CurrencyUi>
    )

    fun show(saveButton: ChangeVisibility) = saveButton.hide()

    object Empty : SettingsUiState {

        override fun show(
            currencyFromAdapter: UpdateAdapter<CurrencyUi>,
            currencyToAdapter: UpdateAdapter<CurrencyUi>
        ) {
            currencyToAdapter.update(listOf(CurrencyUi.Empty))
        }
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

        override fun show(saveButton: ChangeVisibility) {
            saveButton.show()
        }
    }
}