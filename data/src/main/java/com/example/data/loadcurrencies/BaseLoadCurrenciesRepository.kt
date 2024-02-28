package com.example.data.loadcurrencies

import com.example.data.core.ProvideResources
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.domain.loadcurrencies.LoadCurrenciesRepository
import com.example.domain.loadcurrencies.LoadCurrenciesResult
import java.net.UnknownHostException

class BaseLoadCurrenciesRepository(
    private val cloudDataSource: LoadCurrenciesCloudDataSource,
    private val cacheDataSource: CurrenciesCacheDataSource,
    private val provideResources: ProvideResources
) : LoadCurrenciesRepository {

    override suspend fun loadCurrencies(): LoadCurrenciesResult {
        return try {
            if (cacheDataSource.currencies().isEmpty()) {
                cacheDataSource.saveCurrencies(cloudDataSource.currencies())
            }
            LoadCurrenciesResult.Success
        } catch (e: Exception) {
            val message = if (e is UnknownHostException) {
                provideResources.noInternetConnectionMessage()
            } else {
                provideResources.serviceUnavailableMessage()
            }
            LoadCurrenciesResult.Error(message = message)
        }
    }
}