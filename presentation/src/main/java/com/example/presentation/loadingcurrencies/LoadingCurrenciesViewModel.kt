package com.example.presentation.loadingcurrencies

import com.example.domain.CurrenciesRepository
import com.example.domain.LoadCurrenciesResult
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.RunAsync
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation

class LoadingCurrenciesViewModel(
    private val navigation: Navigation.Update,
    private val communication: LoadingCurrenciesCommunication,
    private val repository: CurrenciesRepository,
    runAsync: RunAsync,
    private val mapper: LoadCurrenciesResult.Mapper = BaseLoadCurrenciesResult(
        communication,
        navigation
    )
) : BaseViewModel(runAsync), ProvideLiveData<LoadingCurrenciesUiState> {

    fun init() {
        if (repository.currencies().isEmpty()) {
            loadCurrencies()
        } else {
            navigation.updateUi(DashboardScreen)
        }
    }

    fun loadCurrencies() {
        communication.updateUi(LoadingCurrenciesUiState.Loading)
        runAsync({
            repository.loadCurrencies()
        }) { loadResult ->
            loadResult.map(mapper)
        }
    }

    override fun liveData() = communication.liveData()
}

