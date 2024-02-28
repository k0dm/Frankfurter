package com.example.data.dashboard

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.core.FakeCurrencyConverterCloudDataSource
import com.example.data.dashboard.core.FakeCurrentDate
import com.example.data.dashboard.core.FakeFavoriteCurrenciesCacheDataSource
import com.example.domain.dashboard.DashboardItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class DashboardItemsDatasourceTest {

    private lateinit var mapper: DashboardItemsDatasource
    private lateinit var favoriteCurrenciesCacheDataSource: FakeFavoriteCurrenciesCacheDataSource
    private lateinit var currencyConverterCloudDataSource: FakeCurrencyConverterCloudDataSource
    private lateinit var currentDate: CurrentDate

    @Before
    fun setUp() {
        favoriteCurrenciesCacheDataSource = FakeFavoriteCurrenciesCacheDataSource()
        currencyConverterCloudDataSource = FakeCurrencyConverterCloudDataSource()
        currentDate = FakeCurrentDate(date = "1/1/2024")
        mapper = DashboardItemsDatasource.Base(
            currencyConverterCloudDataSource = currencyConverterCloudDataSource,
            favoriteCacheDataSource = favoriteCurrenciesCacheDataSource,
            currentDate = currentDate
        )
    }

    @Test
    fun testValidCurrencyPairs(): Unit = runBlocking {
        val actualList = mapper.map(
            listOf(
                CurrencyPairEntity("1", "2", 2.0, "1/1/2024"),
                CurrencyPairEntity("3", "4", 1.2, "1/1/2024")
            )
        )
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())
        assertEquals(
            listOf(
                DashboardItem.Base("1", "2", 2.0),
                DashboardItem.Base("3", "4", 1.2)
            ), actualList
        )
    }

    @Test
    fun testInvalidCurrencyPairs(): Unit = runBlocking {
        val actualList = mapper.map(
            listOf(
                CurrencyPairEntity("1", "2", 2.0, "4/2/1999"),
                CurrencyPairEntity("3", "4", -1.0, "")
            )
        )
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            listOf(
                CurrencyPairEntity("1", "2", 99.9, "1/1/2024"),
                CurrencyPairEntity("3", "4", 99.9, "1/1/2024")
            )
        )
        assertEquals(
            listOf(
                DashboardItem.Base("1", "2", 99.9),
                DashboardItem.Base("3", "4", 99.9)
            ), actualList
        )
    }

    @Test(expected = UnknownHostException::class)
    fun testInvalidCurrencyPairsAndNoInternet(): Unit = runBlocking {
        currencyConverterCloudDataSource.returnFailureNoInternet()

        val actualList = mapper.map(
            listOf(
                CurrencyPairEntity("1", "2", 2.0, "4/2/1999"),
                CurrencyPairEntity("3", "4", -1.0, "")
            )
        )
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())
        assertEquals(
            listOf(
                DashboardItem.Base("1", "2", 99.9),
                DashboardItem.Base("3", "4", 99.9)
            ), actualList
        )
    }


    @Test(expected = RuntimeException::class)
    fun testInvalidCurrencyPairsAndServiceUnavailable(): Unit = runBlocking {
        currencyConverterCloudDataSource.returnFailureServiceUnavailable()

        val actualList = mapper.map(
            listOf(
                CurrencyPairEntity("1", "2", 2.0, "4/2/1999"),
                CurrencyPairEntity("3", "4", -1.0, "")
            )
        )
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())
        assertEquals(
            listOf(
                DashboardItem.Base("1", "2", 99.9),
                DashboardItem.Base("3", "4", 99.9)
            ), actualList
        )
    }
}