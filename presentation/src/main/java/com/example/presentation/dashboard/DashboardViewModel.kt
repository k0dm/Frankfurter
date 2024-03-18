package com.example.presentation.dashboard

import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.RunAsync
import com.example.presentation.main.Navigation
import com.example.presentation.settings.SettingsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val navigation: Navigation.Update,
    private val communication: DashboardCommunication,
    private val repository: DashboardRepository,
    private val foregroundWrapper: ForegroundDownloadWorkManagerWrapper,
    runAsync: RunAsync,
    private val currencyPairDelimiter: CurrencyPairDelimiter.Mutable,
    private val mapper: DashboardResult.Mapper
) : BaseViewModel(runAsync), ProvideLiveData<DashboardUiState>, ClickActions {

    fun init() {
        communication.updateUi(DashboardUiState.Progress)
        runAsync({
            foregroundWrapper.start()
        }) {}
    }

    fun goToSettings() = navigation.updateUi(SettingsScreen)

    override fun retry() = init()

    fun removePair(from: String, to: String) = runAsync({
        repository.removePair(from, to)
    }) { dashboardResult ->
        dashboardResult.map(mapper)
    }

    override fun liveData() = communication.liveData()

    override fun openDeletePairDialog(currencyPair: String) =
        navigation.updateUi(currencyPairDelimiter.makeDeletePairScreen(currencyPair))
}