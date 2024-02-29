package com.example.frankfurter

import com.example.frankfurter.modules.DashboardModule
import com.example.frankfurter.modules.LoadingCurrenciesModule
import com.example.frankfurter.modules.MainModule
import com.example.frankfurter.modules.Module
import com.example.presentation.core.CustomViewModel
import com.example.presentation.dashboard.DashboardViewModel
import com.example.presentation.loadingcurrencies.LoadingCurrenciesViewModel
import com.example.presentation.main.MainViewModel

interface ProvideModule {

    fun <T : CustomViewModel> module(clazz: Class<out T>): Module<T>

    @Suppress("UNCHECKED_CAST")
    class Base(private val core: Core) : ProvideModule {

        override fun <T : CustomViewModel> module(clazz: Class<out T>) = when (clazz) {
            MainViewModel::class.java -> MainModule(core)
            LoadingCurrenciesViewModel::class.java -> LoadingCurrenciesModule(core)
            DashboardViewModel::class.java -> DashboardModule(core)
            else -> throw IllegalStateException("No such viewModel with class: $clazz")
        } as Module<T>
    }
}