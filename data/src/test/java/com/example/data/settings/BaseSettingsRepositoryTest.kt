package com.example.data.settings

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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
    fun testAllCurrencies(): Unit = runBlocking {
        val currencies = repository.allCurrencies()
        assertEquals(listOf("1", "2", "3", "4", "5"), currencies)
    }

    @Test
    fun testSave(): Unit = runBlocking {
        repository.save(from = "JPY", to = "EUR")

        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            pairs = listOf(
                CurrencyPairEntity(
                    fromCurrency = "JPY",
                    toCurrency = "EUR",
                    rates = -1.0,
                    date = ""
                )
            )
        )

        repository.save(from = "JPY", to = "USD")

        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            pairs = listOf(
                CurrencyPairEntity(
                    fromCurrency = "JPY",
                    toCurrency = "EUR",
                    rates = -1.0,
                    date = ""
                ),
                CurrencyPairEntity(
                    fromCurrency = "JPY",
                    toCurrency = "USD",
                    rates = -1.0,
                    date = ""
                )
            )
        )
    }

    @Test
    fun testAvailableDestinationsAndSave(): Unit = runBlocking {
        var availableCurrencies = repository.availableDestinations("1")
        assertEquals(listOf("2", "3", "4", "5"), availableCurrencies)

        repository.save(from = "1", to = "5")

        availableCurrencies = repository.availableDestinations("1")
        assertEquals(listOf("2", "3", "4"), availableCurrencies)

        availableCurrencies = repository.availableDestinations("2")
        assertEquals(listOf("1", "3", "4", "5"), availableCurrencies)

        repository.save(from = "2", to = "1")
        repository.save(from = "2", to = "3")
        repository.save(from = "2", to = "4")
        repository.save(from = "2", to = "5")

        availableCurrencies = repository.availableDestinations("1")
        assertEquals(listOf("2", "3", "4"), availableCurrencies)

        availableCurrencies = repository.availableDestinations("2")
        assertEquals(listOf<String>(), availableCurrencies)
    }
}

class FakeReadCurrenciesCacheDataSource : CurrenciesCacheDataSource.Read {

    override suspend fun currencies(): List<String> {
        return listOf("1", "2", "3", "4", "5")
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
        Assert.assertEquals(pairs, savedCurrencyPairs)
    }
}