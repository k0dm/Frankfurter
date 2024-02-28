package com.example.data.dashboard

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import com.example.domain.dashboard.DashboardItem

interface DashboardItemMapper {

    suspend fun map(favoriteCurrencies: List<CurrencyPairEntity>): List<DashboardItem>

    class Base(
        private val currencyConverterCloudDataSource: CurrencyConverterCloudDataSource,
        private val favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Save,
        private val currentDate: CurrentDate
    ) : DashboardItemMapper {

        override suspend fun map(favoriteCurrencies: List<CurrencyPairEntity>): List<DashboardItem> {
            return favoriteCurrencies.map {
                val rates = if (it.isInvalidRate(currentDate)) {
                    val newRates = currencyConverterCloudDataSource.exchangeRate(
                        it.fromCurrency,
                        it.toCurrency
                    )
                    favoriteCacheDataSource.save(
                        CurrencyPairEntity(
                            it.fromCurrency,
                            it.toCurrency,
                            newRates,
                            currentDate.date()
                        )
                    )
                    newRates
                } else {
                    it.rates
                }
                DashboardItem.Base(
                    fromCurrency = it.fromCurrency,
                    toCurrency = it.toCurrency,
                    rates = rates
                )
            }
        }
    }
}