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


    fun init() = runAsync({
        repository.allCurrencies()
    }) { currencies ->
        communication.updateUi(
            SettingsUiState.Initial(fromCurrencies = currencies.map { CurrencyUi.Base(value = it) })
        )
    }

    fun chooseFrom(currency: String) = runAsync({
        val currencies = repository.availableDestinations(currency)
        SettingsUiState.AvailableDestinations(
            fromCurrencies = repository.allCurrencies().map {
                CurrencyUi.Base(value = it, chosen = it == currency)
            },
            toCurrencies = currencies.run {
                if (isEmpty()) listOf(CurrencyUi.Empty)
                else map { CurrencyUi.Base(value = it) }
            }
        )

    }) { uiState ->

        communication.updateUi(uiState)
    }

    fun chooseTo(from: String, to: String) = runAsync({
        SettingsUiState.ReadyToSave(
            toCurrencies = repository.availableDestinations(from).map {
                CurrencyUi.Base(value = it, chosen = it == to)
            }
        )
    }) { uiState ->
        communication.updateUi(uiState)
    }

    fun save(from: String, to: String) = runAsync({
        repository.save(from, to)
    }) {
        goToDashboard()
    }

    fun goToDashboard() {
        clearViewModel.clear(SettingsViewModel::class.java)
        navigation.updateUi(DashboardScreen)
    }
}
