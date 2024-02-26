package com.example.data.currencies

import com.example.data.core.ProvideResources
import com.example.data.currencies.cloud.CurrenciesService
import com.example.data.currencies.data.CurrenciesDao
import com.example.data.currencies.data.CurrencyEntity
import com.example.domain.LoadCurrenciesRepository
import com.example.domain.LoadCurrenciesResult
import java.net.UnknownHostException

class BaseLoadCurrenciesRepository(
    private val cloudDataSource: CurrenciesService,
    private val cacheDataSource: CurrenciesDao,
    private val provideResources: ProvideResources
) : LoadCurrenciesRepository {

    override suspend fun loadCurrencies(): LoadCurrenciesResult {
        return try {
            if (cacheDataSource.currencies().isEmpty()) {
                cloudDataSource.currencies().execute().body()!!.keys.forEach {
                    cacheDataSource.saveCurrencies(CurrencyEntity(it))
                }
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