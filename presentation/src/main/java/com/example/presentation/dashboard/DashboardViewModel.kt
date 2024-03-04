package com.example.presentation.dashboard

import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.RunAsync
import com.example.presentation.main.Navigation
import com.example.presentation.settings.SettingsScreen

class DashboardViewModel(
    private val navigation: Navigation.Update,
    private val communication: DashboardCommunication,
    private val repository: DashboardRepository,
    runAsync: RunAsync,
    private val clearViewModel: ClearViewModel,
    private val concurrencyPairDelimiter: ConcurrencyPairDelimiter.Mutable,
    private val mapper: DashboardResult.Mapper = BaseDashboardResultMapper(
        concurrencyPairDelimiter, communication
    )
) : BaseViewModel(runAsync), ProvideLiveData<DashboardUiState>, ClickActions {

    fun init() {
        communication.updateUi(DashboardUiState.Progress)
        runAsync({
            repository.dashboards()
        }) { dashboardResult ->
            dashboardResult.map(mapper)
        }
    }

    fun goToSettings() {
        clearViewModel.clear(DashboardViewModel::class.java)
        navigation.updateUi(SettingsScreen)
    }

    override fun retry() = init()

    fun removePair(from: String, to: String) = runAsync({
        repository.removePair(from, to)
    }) { dashboardResult ->
        dashboardResult.map(mapper)
    }

    override fun liveData() = communication.liveData()

    override fun openDeletePairDialog(currencyPair: String) {
        navigation.updateUi(concurrencyPairDelimiter.makeDeletePairScreen(currencyPair))
    }
}