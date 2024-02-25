package com.example.presentation.loadingcurrencies

interface LoadingCurrenciesUiState {

    object Loading: LoadingCurrenciesUiState

    data class Error(private val message: String): LoadingCurrenciesUiState

    object Empty: LoadingCurrenciesUiState
}