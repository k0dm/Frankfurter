package com.example.presentation.dashboard.adapter

import com.example.presentation.core.views.ChangeText
import com.example.presentation.dashboard.ClickActions

interface DashboardCurrencyPairUi {

    fun type(): DashboardTypeUi

    fun showError(errorTextView: ChangeText) = Unit

    fun showCurrencyPair(currencyPairText: ChangeText, ratesTextView: ChangeText) = Unit

    fun delete(viewModel: ClickActions) = Unit

    data class Base(
        private val currencyPair: String,
        private val rates: String
    ) : DashboardCurrencyPairUi {

        override fun type() = DashboardTypeUi.CurrencyPair

        override fun showCurrencyPair(currencyPairText: ChangeText, ratesTextView: ChangeText) {
            currencyPairText.changeText(currencyPair)
            ratesTextView.changeText(rates)
        }

        override fun delete(viewModel: ClickActions) {
            viewModel.openDeletePairDialog(currencyPair)
        }
    }

    object Progress : DashboardCurrencyPairUi {

        override fun type() = DashboardTypeUi.Progress
    }

    data class Error(private val message: String) : DashboardCurrencyPairUi {

        override fun type() = DashboardTypeUi.Error

        override fun showError(errorTextView: ChangeText) {
            errorTextView.changeText(message)
        }
    }

    object Empty : DashboardCurrencyPairUi {

        override fun type(): DashboardTypeUi = DashboardTypeUi.Empty
    }
}