package com.example.presentation.loadingcurrencies

import com.example.domain.LoadCurrenciesRepository
import com.example.domain.LoadCurrenciesResult
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.RunAsync
import com.example.presentation.main.Navigation

class LoadingCurrenciesViewModel(
    private val navigation: Navigation.Update,
    private val communication: LoadingCurrenciesCommunication,
    private val repository: LoadCurrenciesRepository,
    runAsync: RunAsync,
    private val clearViewModel: ClearViewModel,
    private val mapper: LoadCurrenciesResult.Mapper = BaseLoadCurrenciesResult(
        communication,
        navigation,
        clearViewModel
    )
) : BaseViewModel(runAsync), ProvideLiveData<LoadingCurrenciesUiState> {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) loadCurrencies()
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

