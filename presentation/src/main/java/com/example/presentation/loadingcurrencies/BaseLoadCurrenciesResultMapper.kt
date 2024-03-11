package com.example.presentation.loadingcurrencies

import com.example.domain.loadcurrencies.LoadCurrenciesResult
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseLoadCurrenciesResultMapper @Inject constructor(
    private val communication: LoadingCurrenciesCommunication,
    private val navigation: Navigation.Update,
) : LoadCurrenciesResult.Mapper {

    override fun mapSuccess() {
        navigation.updateUi(DashboardScreen)
    }

    override fun mapError(message: String) {
        communication.updateUi(LoadingCurrenciesUiState.Error(message))
    }
}