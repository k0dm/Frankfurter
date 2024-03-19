package com.example.data.settings

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.domain.settings.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseSettingsRepository @Inject constructor(
    private val currenciesCacheDataSource: CurrenciesCacheDataSource.Read,
    private val favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.ReadAndSave
) : SettingsRepository {

    override suspend fun allCurrencies() = currenciesCacheDataSource.currencies()

    override suspend fun availableDestinations(from: String): List<String> {
        val allCurrencies = allCurrencies().toMutableList().also { it.remove(from) }
        val savedCurrencies = favoriteCurrenciesCacheDataSource.favoriteCurrencies()

        val matched = savedCurrencies
            .filter { it.fromCurrency == from }
            .map { it.toCurrency }
        allCurrencies.removeAll(matched)
        return allCurrencies
    }

    override suspend fun save(from: String, to: String) {
        favoriteCurrenciesCacheDataSource.save(CurrencyPairEntity(from, to))
    }

    override suspend fun savedPairsCount() =
        favoriteCurrenciesCacheDataSource.favoriteCurrencies().size
}