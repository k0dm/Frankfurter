package com.example.presentation.dashboard

import com.example.domain.dashboard.DashboardItem
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseDashboardItemMapper @Inject constructor(
    private val currencyPairDelimiter: CurrencyPairDelimiter.AddDelimiter,
    private val ratesFormatter: RatesFormatter = RatesFormatter.Base()
) : DashboardItem.Mapper<DashboardCurrencyPairUi> {

    override fun map(fromCurrency: String, toCurrency: String, rates: Double) =
        DashboardCurrencyPairUi.Base(
            currencyPairDelimiter.addDelimiter(fromCurrency, toCurrency),
            ratesFormatter.format(rates)
        )
}