package com.example.frankfurter.modules

import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.frankfurter.Core
import com.example.frankfurter.ProvideInstance
import com.example.presentation.settings.SettingsCommunication
import com.example.presentation.settings.SettingsViewModel

class SettingsModule(
    private val core: Core,
    private val provideInstance: ProvideInstance
) : Module<SettingsViewModel> {

    override fun viewModel() = SettingsViewModel(
        navigation = core.navigation(),
        communication = SettingsCommunication.Base(),
        repository = provideInstance.provideSettingsRepository(
            currenciesCacheDataSource = CurrenciesCacheDataSource.Base(
                core.database().currenciesDao()
            ),
            favoriteCurrenciesCacheDataSource = FavoriteCurrenciesCacheDataSource.Base(
                core.database().favoriteCurrenciesDao()
            ),
        ),
        runAsync = core.runAsync(),
        clearViewModel = core.clearViewModel()
    )
}