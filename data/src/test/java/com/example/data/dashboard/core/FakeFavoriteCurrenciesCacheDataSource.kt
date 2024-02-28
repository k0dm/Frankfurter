package com.example.data.dashboard.core

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import org.junit.Assert

internal class FakeFavoriteCurrenciesCacheDataSource : FavoriteCurrenciesCacheDataSource.Mutable {

    private var hasCache = false
    private var validData = true

    override suspend fun favoriteCurrencies(): List<CurrencyPairEntity> {
        return if (hasCache) {
            val dates = if (validData) {
                Pair("1/1/2024", "1/1/2024")
            } else {
                Pair("15/3/2020", "1/1/2024")
            }

            listOf(
                CurrencyPairEntity("A", "B", 2.0, dates.first),
                CurrencyPairEntity("C", "D", 1.3, dates.second)
            )
        } else {
            emptyList()
        }
    }

    fun hasValidCache() {
        hasCache = true
        validData = true
    }

    fun hasInvalidCache() {
        hasCache = true
        validData = false
    }

    private var savedCurrencyPairs = mutableListOf<CurrencyPairEntity>()

    override suspend fun save(currencyPair: CurrencyPairEntity) {
        savedCurrencyPairs.add(currencyPair)
    }

    fun checkSavedCurrencyPairs(pairs: List<CurrencyPairEntity>) {
        Assert.assertEquals(pairs, savedCurrencyPairs)
    }
}