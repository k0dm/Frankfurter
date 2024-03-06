package com.example.frankfurter.modules

import com.example.data.dashboard.DashboardItemsDatasource
import com.example.data.dashboard.HandleError
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import com.example.data.dashboard.cloud.CurrencyConverterService
import com.example.frankfurter.Core
import com.example.frankfurter.ProvideInstance
import com.example.presentation.dashboard.CurrencyPairDelimiter
import com.example.presentation.dashboard.DashboardCommunication
import com.example.presentation.dashboard.DashboardViewModel

class DashboardModule(
    private val core: Core,
    private val provideInstance: ProvideInstance
) : Module<DashboardViewModel> {

    override fun viewModel(): DashboardViewModel {

        val favoriteCacheDataSource = FavoriteCurrenciesCacheDataSource.Base(
            core.database().favoriteCurrenciesDao()
        )

        return DashboardViewModel(
            navigation = core.navigation(),
            communication = DashboardCommunication.Base(),
            repository = provideInstance.provideDashboardRepository(
                favoriteCacheDataSource = favoriteCacheDataSource,
                dashboardItemsDatasource = DashboardItemsDatasource.Base(
                    currencyConverterCloudDataSource = CurrencyConverterCloudDataSource.Base(
                        core.retrofit().create(CurrencyConverterService::class.java)
                    ),
                    favoriteCacheDataSource = favoriteCacheDataSource,
                    currentDate = CurrentDate.Base(),
                ),
                handleError = HandleError.Base(core.provideResources())
            ),
            runAsync = core.runAsync(),
            clearViewModel = core.clearViewModel(),
            currencyPairDelimiter = CurrencyPairDelimiter.Base()
        )
    }
}