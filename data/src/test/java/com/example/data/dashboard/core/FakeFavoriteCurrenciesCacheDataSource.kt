package com.example.data.dashboard.core

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import org.junit.Assert

internal class FakeFavoriteCurrenciesCacheDataSource : FavoriteCurrenciesCacheDataSource.Mutable {

    private val actualCurrencyPairs = mutableListOf<CurrencyPairEntity>()

    override suspend fun favoriteCurrencies(): List<CurrencyPairEntity> {
        return actualCurrencyPairs
    }

    fun hasValidCache() = with(actualCurrencyPairs) {
        add(CurrencyPairEntity("A", "B", 2.0, "1/1/2024"))
        add(CurrencyPairEntity("C", "D", 1.3, "1/1/2024"))
    }

    fun hasInvalidCache() = with(actualCurrencyPairs) {
        add(CurrencyPairEntity("A", "B", 2.0, "15/3/2020"))
        add(CurrencyPairEntity("C", "D", 1.3, "1/1/2024"))
    }

    override suspend fun save(currencyPair: CurrencyPairEntity) {
        actualCurrencyPairs.add(currencyPair)
    }

    fun checkSavedCurrencyPairs(vararg pairs: CurrencyPairEntity) {
        Assert.assertEquals(pairs.toList(), actualCurrencyPairs)
    }

    override suspend fun delete(currencyPair: CurrencyPairEntity) {
        actualCurrencyPairs.remove(currencyPair)
    }
}