package com.example.presentation.dashboard.adapter

import com.example.presentation.core.views.ChangeText

interface DashboardCurrencyPairUi {

    fun type(): DashboardTypeUi

    fun showText(views: List<ChangeText>) = Unit

    data class Base(
        private val fromCurrency: String,
        private val toCurrency: String,
        private val rates: Double
    ) : DashboardCurrencyPairUi {

        override fun type() = DashboardTypeUi.CurrencyPair

        override fun showText(views: List<ChangeText>) {
            views[0].changeText(fromCurrency)
            views[1].changeText(toCurrency)
            views[2].changeText(rates.toString()) // todo fix
        }
    }

    object Progress : DashboardCurrencyPairUi {

        override fun type() = DashboardTypeUi.Progress
    }

    data class Error(private val message: String) : DashboardCurrencyPairUi {

        override fun type() = DashboardTypeUi.Error

        override fun showText(views: List<ChangeText>) {
            views[0].changeText(message)
        }
    }

    object Empty : DashboardCurrencyPairUi {

        override fun type(): DashboardTypeUi = DashboardTypeUi.Empty
    }
}