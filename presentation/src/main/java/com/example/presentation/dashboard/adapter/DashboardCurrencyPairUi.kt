package com.example.presentation.dashboard.adapter

import com.example.presentation.core.views.ChangeText
import com.example.presentation.dashboard.ClickActions

interface DashboardCurrencyPairUi {

    fun type(): DashboardTypeUi

    fun showError(errorTextView: ChangeText) = Unit

    fun showCurrencyPair(currencyPairText: ChangeText, ratesTextView: ChangeText) = Unit

    fun delete(viewModel: ClickActions, cancelBlock: () -> Unit) = Unit

    data class Base(
        private val currencyPair: String,
        private val rates: String
    ) : DashboardCurrencyPairUi {

        override fun type() = DashboardTypeUi.CurrencyPair

        override fun showCurrencyPair(currencyPairText: ChangeText, ratesTextView: ChangeText) {
            currencyPairText.changeText(currencyPair)
            ratesTextView.changeText(rates)
        }

        override fun delete(viewModel: ClickActions, cancelBlock: () -> Unit) {
            val dividerIndex = currencyPair.indexOf(" / ")
            viewModel.openDeletePairDialog(
                currencyPair.substring(0 until dividerIndex),
                currencyPair.substring(dividerIndex + 3),
                cancelBlock
            )
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