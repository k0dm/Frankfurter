package com.example.presentation.loadingcurrencies

import com.example.domain.loadcurrencies.LoadCurrenciesRepository
import com.example.domain.loadcurrencies.LoadCurrenciesResult
import com.example.presentation.core.BaseViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.RunAsync
import com.example.presentation.main.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingCurrenciesViewModel @Inject constructor(
    private val navigation: Navigation.Update,
    private val communication: LoadingCurrenciesCommunication,
    private val repository: LoadCurrenciesRepository,
    runAsync: RunAsync,
    private val mapper: LoadCurrenciesResult.Mapper = BaseLoadCurrenciesResultMapper(
        communication,
        navigation,
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