package com.example.presentation.loadingcurrencies

import com.example.presentation.core.LiveDataWrapper
import javax.inject.Inject
import javax.inject.Singleton

interface LoadingCurrenciesCommunication : LiveDataWrapper<LoadingCurrenciesUiState> {

    @Singleton
    class Base @Inject constructor() : LoadingCurrenciesCommunication,
        LiveDataWrapper.Single<LoadingCurrenciesUiState>()
}