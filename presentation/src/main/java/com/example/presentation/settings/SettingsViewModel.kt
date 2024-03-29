package com.example.presentation.settings

import com.example.domain.settings.SaveResult
import com.example.domain.settings.SettingsInteractor
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.RunAsync
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigation: Navigation.Update,
    private val communication: SettingsCommunication,
    private val interactor: SettingsInteractor,
    runAsync: RunAsync,
    private val mapper: SaveResult.Mapper = BaseSaveResultMapper(navigation)
) : BaseViewModel(runAsync), ProvideLiveData<SettingsUiState> {

    fun init(bundleWrapper: SettingsBundleWrapper) {
        if (bundleWrapper.isEmpty() || bundleWrapper.restore().first.isBlank()) {
            runAsync({
                interactor.allCurrencies()
            }) { currencies ->
                communication.updateUi(
                    SettingsUiState.Initial(fromCurrencies = currencies.map { CurrencyUi.Base(value = it) })
                )
            }
        } else {
            bundleWrapper.restore().run {
                chooseFrom(first) {
                    if (second.isNotBlank()) {
                        chooseTo(first, second)
                    }
                }
            }
        }
    }

    fun chooseFrom(currency: String, block: () -> Unit = {}) = runAsync({
        val currencies = interactor.availableDestinations(currency)
        SettingsUiState.AvailableDestinations(
            fromCurrencies = interactor.allCurrencies().map {
                CurrencyUi.Base(value = it, chosen = it == currency)
            },
            toCurrencies = currencies
                .map { CurrencyUi.Base(value = it) }
                .ifEmpty { listOf(CurrencyUi.Empty) }
        )
    }) { uiState ->
        communication.updateUi(uiState)
        block.invoke()
    }

    fun chooseTo(from: String, to: String) = runAsync({
        SettingsUiState.ReadyToSave(
            toCurrencies = interactor.availableDestinations(from).map {
                CurrencyUi.Base(value = it, chosen = it == to)
            }
        )
    }) { uiState ->
        communication.updateUi(uiState)
    }

    fun save(from: String, to: String) = runAsync({
        interactor.save(from, to)
    }) { saveResult ->
        saveResult.map(mapper)
    }

    fun goToDashboard() = navigation.updateUi(DashboardScreen)

    override fun liveData() = communication.liveData()
}