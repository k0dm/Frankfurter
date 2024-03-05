package com.example.frankfurter

import com.example.frankfurter.modules.DashboardModule
import com.example.frankfurter.modules.LoadingCurrenciesModule
import com.example.frankfurter.modules.MainModule
import com.example.frankfurter.modules.Module
import com.example.frankfurter.modules.SettingsModule
import com.example.presentation.core.CustomViewModel
import com.example.presentation.dashboard.DashboardViewModel
import com.example.presentation.loadingcurrencies.LoadingCurrenciesViewModel
import com.example.presentation.main.MainViewModel
import com.example.presentation.settings.SettingsViewModel

interface ProvideModule {

    fun <T : CustomViewModel> module(clazz: Class<out T>): Module<T>

    @Suppress("UNCHECKED_CAST")
    class Base(
        private val core: Core,
        private val provideInstance: ProvideInstance
    ) : ProvideModule {

        override fun <T : CustomViewModel> module(clazz: Class<out T>) = when (clazz) {
            MainViewModel::class.java -> MainModule(core)
            LoadingCurrenciesViewModel::class.java -> LoadingCurrenciesModule(core, provideInstance)
            DashboardViewModel::class.java -> DashboardModule(core, provideInstance)
            SettingsViewModel::class.java -> SettingsModule(core, provideInstance)
            else -> throw IllegalStateException("No such viewModel with class: $clazz")
        } as Module<T>
    }
}