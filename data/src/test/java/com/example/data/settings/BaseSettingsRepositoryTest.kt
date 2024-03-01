package com.example.data.settings

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BaseSettingsRepositoryTest {

    private lateinit var repository: BaseSettingsRepository
    private lateinit var currenciesCacheDataSource: FakeReadCurrenciesCacheDataSource
    private lateinit var favoriteCurrenciesCacheDataSource: FakeFavoriteCurrenciesCacheDataSource

    @Before
    fun setUp() {
        currenciesCacheDataSource = FakeReadCurrenciesCacheDataSource()
        favoriteCurrenciesCacheDataSource = FakeFavoriteCurrenciesCacheDataSource()
        repository = BaseSettingsRepository(
            currenciesCacheDataSource = currenciesCacheDataSource,
            favoriteCurrenciesCacheDataSource = favoriteCurrenciesCacheDataSource
        )
    }

    @Test
    fun testMainScenario(): Unit = runBlocking {
        var availableCurrencies = repository.availableDestinations("USD")
        assertEquals(listOf("EUR", "JPY", "AUD", "BRL"), availableCurrencies)

        repository.save(from = "USD", to = "BRL")
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            pairs = listOf(CurrencyPairEntity(fromCurrency = "USD", toCurrency = "BRL"))
        )

        repository.save(from = "USD", to = "AUD")
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            pairs = listOf(
                CurrencyPairEntity(fromCurrency = "USD", toCurrency = "BRL"),
                CurrencyPairEntity(fromCurrency = "USD", toCurrency = "AUD")
            )
        )

        availableCurrencies = repository.availableDestinations("JPY")
        assertEquals(listOf("USD", "EUR", "AUD", "BRL"), availableCurrencies)

        availableCurrencies = repository.availableDestinations("USD")
        assertEquals(listOf("EUR", "JPY"), availableCurrencies)

        repository.save(from = "USD", to = "EUR")
        repository.save(from = "USD", to = "JPY")

        availableCurrencies = repository.availableDestinations("AUD")
        assertEquals(listOf("USD", "EUR", "JPY", "BRL"), availableCurrencies)

        availableCurrencies = repository.availableDestinations("USD")
        assertEquals(emptyList<String>(), availableCurrencies)
    }
}

private class FakeReadCurrenciesCacheDataSource : CurrenciesCacheDataSource.Read {

    override suspend fun currencies(): List<String> {
        return listOf("USD", "EUR", "JPY", "AUD", "BRL")
    }
}

private class FakeFavoriteCurrenciesCacheDataSource : FavoriteCurrenciesCacheDataSource.Mutable {

    private var savedCurrencyPairs = mutableListOf<CurrencyPairEntity>()

    override suspend fun favoriteCurrencies(): List<CurrencyPairEntity> {
        return savedCurrencyPairs
    }

    override suspend fun save(currencyPair: CurrencyPairEntity) {
        savedCurrencyPairs.add(currencyPair)
    }

    fun checkSavedCurrencyPairs(pairs: List<CurrencyPairEntity>) {
        assertEquals(pairs, savedCurrencyPairs)
    }
}