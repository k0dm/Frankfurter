package com.example.presentation.main

import com.example.presentation.core.CustomViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.loadingcurrencies.LoadingCurrenciesScreen

class MainViewModel(
    private val navigation: Navigation.Mutable
) : CustomViewModel, ProvideLiveData<Screen> {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            navigation.updateUi(LoadingCurrenciesScreen)
        }
    }

    override fun liveData() = navigation.liveData()
}

