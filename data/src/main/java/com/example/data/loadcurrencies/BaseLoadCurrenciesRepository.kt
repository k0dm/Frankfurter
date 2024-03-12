package com.example.data.loadcurrencies

import com.example.data.dashboard.HandleError
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.domain.loadcurrencies.LoadCurrenciesRepository
import com.example.domain.loadcurrencies.LoadCurrenciesResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseLoadCurrenciesRepository @Inject constructor(
    private val cloudDataSource: LoadCurrenciesCloudDataSource,
    private val cacheDataSource: CurrenciesCacheDataSource.Mutable,
    private val handleError: HandleError
) : LoadCurrenciesRepository {

    override suspend fun loadCurrencies(): LoadCurrenciesResult {
        return try {
            if (cacheDataSource.currencies().isEmpty()) {
                cacheDataSource.saveCurrencies(cloudDataSource.currencies())
            }
            LoadCurrenciesResult.Success
        } catch (e: Exception) {
            LoadCurrenciesResult.Error(message = handleError.handle(e))
        }
    }
}