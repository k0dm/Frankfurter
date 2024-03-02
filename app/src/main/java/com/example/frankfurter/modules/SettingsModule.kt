package com.example.frankfurter.modules

import com.example.data.settings.BaseSettingsRepository
import com.example.frankfurter.Core
import com.example.presentation.settings.SettingsCommunication
import com.example.presentation.settings.SettingsViewModel

class SettingsModule(private val core: Core) : Module<SettingsViewModel> {
    override fun viewModel() = SettingsViewModel(
        navigation = core.navigation(),
        communication = SettingsCommunication.Base(),
        repository = BaseSettingsRepository(
            currenciesCacheDataSource = core.currenciesCacheDataSource(),
            favoriteCurrenciesCacheDataSource = core.favoriteCurrenciesCacheDataSource(),
        ),
        runAsync = core.runAsync(),
        clearViewModel = core.clearViewModel()
    )
}