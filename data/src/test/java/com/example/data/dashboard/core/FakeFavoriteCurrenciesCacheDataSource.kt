package com.example.data.dashboard.core

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import org.junit.Assert

internal class FakeFavoriteCurrenciesCacheDataSource : FavoriteCurrenciesCacheDataSource.Mutable {

    override suspend fun favoriteCurrencies(): List<CurrencyPairEntity> {
        return savedCurrencyPairs
    }

    fun hasValidCache() = with(savedCurrencyPairs) {
        add(CurrencyPairEntity("A", "B", 2.0, "1/1/2024"))
        add(CurrencyPairEntity("C", "D", 1.3, "1/1/2024"))
    }

    fun hasInvalidCache() = with(savedCurrencyPairs) {
        add(CurrencyPairEntity("A", "B", 2.0, "15/3/2020"))
        add(CurrencyPairEntity("C", "D", 1.3, "1/1/2024"))
    }

    private val savedCurrencyPairs = mutableListOf<CurrencyPairEntity>()

    override suspend fun save(currencyPair: CurrencyPairEntity) {
        savedCurrencyPairs.add(currencyPair)
    }

    fun checkSavedCurrencyPairs(vararg pairs: CurrencyPairEntity) {
        Assert.assertEquals(pairs, savedCurrencyPairs)
    }

    override suspend fun delete(currencyPair: CurrencyPairEntity) {
        savedCurrencyPairs.remove(currencyPair)
    }
}