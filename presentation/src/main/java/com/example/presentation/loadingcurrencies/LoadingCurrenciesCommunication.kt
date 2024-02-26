package com.example.presentation.loadingcurrencies

import com.example.presentation.core.LiveDataWrapper

interface LoadingCurrenciesCommunication: LiveDataWrapper<LoadingCurrenciesUiState> {

    class Base : LoadingCurrenciesCommunication, LiveDataWrapper.Single<LoadingCurrenciesUiState>()
}