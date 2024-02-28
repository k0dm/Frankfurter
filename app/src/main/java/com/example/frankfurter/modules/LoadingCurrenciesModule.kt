package com.example.frankfurter.modules

import com.example.data.loadcurrencies.BaseLoadCurrenciesRepository
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.frankfurter.Core
import com.example.presentation.loadingcurrencies.LoadingCurrenciesCommunication
import com.example.presentation.loadingcurrencies.LoadingCurrenciesViewModel

class LoadingCurrenciesModule(private val core: Core) : Module<LoadingCurrenciesViewModel> {

    override fun viewModel() = LoadingCurrenciesViewModel(
        navigation = core.navigation(),
        communication = LoadingCurrenciesCommunication.Base(),
        repository = BaseLoadCurrenciesRepository(
            cloudDataSource = LoadCurrenciesCloudDataSource.Base(),
            cacheDataSource = CurrenciesCacheDataSource.Base(
                core.cacheModule().database().currenciesDao()
            ),
            provideResources = core.provideResources()
        ),
        runAsync = core.runAsync(),
        clearViewModel = core.clearViewModel()
    )
}