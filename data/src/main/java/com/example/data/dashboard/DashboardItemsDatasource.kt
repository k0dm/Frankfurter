package com.example.data.dashboard

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import com.example.domain.dashboard.DashboardItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface DashboardItemsDatasource {

    suspend fun dashboardItems(favoriteCurrencies: List<CurrencyPairEntity>): List<DashboardItem>

    @Singleton
    class Base @Inject constructor(
        private val currencyConverterCloudDataSource: CurrencyConverterCloudDataSource,
        private val favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Save,
        private val currentDate: CurrentDate,
        private val dispatcherIO: CoroutineDispatcher
    ) : DashboardItemsDatasource {

        private val mutex = Mutex()

        override suspend fun dashboardItems(
            favoriteCurrencies: List<CurrencyPairEntity>
        ): List<DashboardItem> = withContext(dispatcherIO) {

            val resultList = mutableListOf<DashboardItem>()

            favoriteCurrencies.map {
                launch {
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
                    mutex.withLock {
                        resultList.add(
                            DashboardItem.Base(
                                fromCurrency = it.fromCurrency,
                                toCurrency = it.toCurrency,
                                rates = rates
                            )
                        )
                    }

                }
            }.joinAll()
            return@withContext resultList
        }
    }
}