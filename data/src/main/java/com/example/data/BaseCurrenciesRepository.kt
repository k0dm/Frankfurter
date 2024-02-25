package com.example.data

import com.example.data.cloud.CurrenciesService
import com.example.data.data.CurrenciesDao
import com.example.data.data.CurrencyEntity
import com.example.domain.CurrenciesRepository
import com.example.domain.LoadCurrenciesResult
import java.net.UnknownHostException

class BaseCurrenciesRepository(
    private val cloudDataSource: CurrenciesService,
    private val cacheDataSource: CurrenciesDao

) : CurrenciesRepository {

    override fun currencies() = cacheDataSource.currencies().map { it.currency }

    override suspend fun loadCurrencies(): LoadCurrenciesResult {
        return try {
            cloudDataSource.currencies().execute().body()!!.keys.forEach {
                cacheDataSource.saveCurrencies(CurrencyEntity(it))
            }
            LoadCurrenciesResult.Success
        } catch (e: Exception) {
            val message = if (e is UnknownHostException) {
                "No internet connection"
            } else {
                "Service unavailable"
            }
            LoadCurrenciesResult.Error(message = message)
        }
    }
}