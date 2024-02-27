package com.example.data.dashboard

import com.example.data.core.ProvideResources
import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import com.example.domain.dashboard.DashBoardResult
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import java.net.UnknownHostException

class BaseDashboardRepository(
    private val cacheDataSource: FavoriteCurrenciesCacheDataSource.Mutable,
    private val currencyConverterCloudDataSource: CurrencyConverterCloudDataSource,
    private val provideResources: ProvideResources
) : DashboardRepository {

    override suspend fun dashboards(): DashBoardResult {

        val favoriteCurrencies = cacheDataSource.favoriteCurrencies()

        return if (favoriteCurrencies.isEmpty()) {
            DashBoardResult.Empty
        } else {
            try {
                val dashboardItems = favoriteCurrencies.map {
                    val rates = if (it.isInvalidRate()) {
                        val newRates = currencyConverterCloudDataSource.exchangeRate(
                            it.fromCurrency,
                            it.toCurrency
                        )
                        cacheDataSource.save(
                            CurrencyPairEntity(
                                it.fromCurrency,
                                it.toCurrency,
                                newRates
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
                DashBoardResult.Success(listOfItems = dashboardItems)
            } catch (e: Exception) {
                val message = if (e is UnknownHostException) {
                    provideResources.noInternetConnectionMessage()
                } else {
                    provideResources.serviceUnavailableMessage()
                }
                DashBoardResult.Error(message = message)
            }
        }
    }
}

