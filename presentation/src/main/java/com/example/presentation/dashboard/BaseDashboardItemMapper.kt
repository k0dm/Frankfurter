package com.example.presentation.dashboard

import com.example.domain.dashboard.DashboardItem
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi

object BaseDashboardItemMapper : DashboardItem.Mapper<DashboardCurrencyPairUi> {
    override fun map(
        fromCurrency: String,
        toCurrency: String,
        rates: Double
    ) = DashboardCurrencyPairUi.Base(fromCurrency, toCurrency, rates)
}