package com.example.presentation.loadingcurrencies

import com.example.presentation.core.LiveDataWrapper
import javax.inject.Inject

interface LoadingCurrenciesCommunication : LiveDataWrapper<LoadingCurrenciesUiState> {

    class Base @Inject constructor() : LoadingCurrenciesCommunication,
        LiveDataWrapper.Single<LoadingCurrenciesUiState>()
}