package com.example.frankfurter.modules

import com.example.data.dashboard.BaseDashboardRepository
import com.example.data.dashboard.DashboardItemsDatasource
import com.example.data.dashboard.HandleError
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import com.example.data.dashboard.cloud.CurrencyConverterService
import com.example.frankfurter.Core
import com.example.presentation.dashboard.DashboardCommunication
import com.example.presentation.dashboard.DashboardViewModel

class DashboardModule(private val core: Core) : Module<DashboardViewModel> {


    override fun viewModel() = DashboardViewModel(
        navigation = core.navigation(),
        communication = DashboardCommunication.Base(),
        repository = BaseDashboardRepository(
            favoriteCacheDataSource = core.favoriteCurrenciesCacheDataSource(),
            dashboardItemsDatasource =
            DashboardItemsDatasource.Base(
                currencyConverterCloudDataSource = CurrencyConverterCloudDataSource.Base(
                    core.retrofit().create(CurrencyConverterService::class.java)
                ),
                favoriteCacheDataSource = core.favoriteCurrenciesCacheDataSource(),
                currentDate = CurrentDate.Base()
            ),
            handleError = HandleError.Base(core.provideResources())
        ),
        runAsync = core.runAsync(),
        clearViewModel = core.clearViewModel()
    )
}
