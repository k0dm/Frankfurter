package com.example.presentation.settings

import com.example.domain.settings.SettingsRepository
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.RunAsync
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation

class SettingsViewModel(
    private val navigation: Navigation.Update,
    private val communication: SettingsCommunication,
    private val repository: SettingsRepository,
    runAsync: RunAsync,
    private val clearViewModel: ClearViewModel
) : BaseViewModel(runAsync), ProvideLiveData<SettingsUiState> {

    private var currencyPair = CurrencyPair()
    private var allCurrencies = listOf<String>()
    private var availableDestinations = listOf<String>()

    fun init() = runAsync({
        repository.allCurrencies().also { allCurrencies = it }
    }) { currencies ->
        communication.updateUi(
            SettingsUiState.Initial(fromCurrencies = currencies.map { CurrencyUi(value = it) })
        )
    }

    fun chooseFrom(currency: String) = runAsync({
        currencyPair.from = currency
        repository.availableDestinations(currency).also { availableDestinations = it }
    }) { currencies ->
        communication.updateUi(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = allCurrencies.map {
                    CurrencyUi(value = it, chosen = it == currency)
                },
                toCurrencies = currencies.map { CurrencyUi(value = it) }
            )
        )
    }

    fun chooseTo(currency: String) {
        currencyPair.to = currency
        communication.updateUi(
            SettingsUiState.ReadyToSave(
                toCurrencies = availableDestinations.map {
                    CurrencyUi(value = it, chosen = it == currency)
                }
            )
        )
    }

    fun save() = runAsync({
        repository.save(currencyPair.from, currencyPair.to)
    }) {
        goToDashboard()
    }

    fun goToDashboard() {
        clearViewModel.clear(SettingsViewModel::class.java)
        navigation.updateUi(DashboardScreen)
    }
}

private data class CurrencyPair(var from: String = "", var to: String = "")

