package com.example.presentation.loadingcurrencies

import com.example.domain.LoadCurrenciesResult
import com.example.presentation.core.ClearViewModel
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation

class BaseLoadCurrenciesResult(
    private val communication: LoadingCurrenciesCommunication,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : LoadCurrenciesResult.Mapper {


    override fun mapSuccess() {
        clearViewModel.clear(LoadingCurrenciesViewModel::class.java)
        navigation.updateUi(DashboardScreen)
    }

    override fun mapError(message: String) {
        communication.updateUi(LoadingCurrenciesUiState.Error(message))
    }
}