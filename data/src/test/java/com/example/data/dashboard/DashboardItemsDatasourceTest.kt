package com.example.data.dashboard

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.core.FakeCurrencyConverterCloudDataSource
import com.example.data.dashboard.core.FakeCurrentDate
import com.example.data.dashboard.core.FakeFavoriteCurrenciesCacheDataSource
import com.example.domain.dashboard.DashboardItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DashboardItemsDatasourceTest {

    private lateinit var dashboardItemsDatasource: DashboardItemsDatasource
    private lateinit var favoriteCurrenciesCacheDataSource: FakeFavoriteCurrenciesCacheDataSource
    private lateinit var currencyConverterCloudDataSource: FakeCurrencyConverterCloudDataSource
    private lateinit var currentDate: CurrentDate
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Before
    fun setUp() {
        favoriteCurrenciesCacheDataSource = FakeFavoriteCurrenciesCacheDataSource()
        currencyConverterCloudDataSource = FakeCurrencyConverterCloudDataSource()
        currentDate = FakeCurrentDate(date = "1/1/2024")
        dashboardItemsDatasource = DashboardItemsDatasource.Base(
            currencyConverterCloudDataSource = currencyConverterCloudDataSource,
            favoriteCacheDataSource = favoriteCurrenciesCacheDataSource,
            currentDate = currentDate,
            dispatcherIO = Dispatchers.Unconfined
        )
    }

    @Test
    fun testValidCurrencyPairs(): Unit = runBlocking {
        val actualList = dashboardItemsDatasource.dashboardItems(
            listOf(
                CurrencyPairEntity("1", "2", 2.0, "1/1/2024"),
                CurrencyPairEntity("3", "4", 1.2, "1/1/2024")
            ),
            viewModelScope
        )
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs()
        assertEquals(
            listOf(
                DashboardItem.Base("1", "2", 2.0),
                DashboardItem.Base("3", "4", 1.2)
            ), actualList
        )
    }

    @Test
    fun testInvalidCurrencyPairs(): Unit = runBlocking {
        val actualList = dashboardItemsDatasource.dashboardItems(
            listOf(
                CurrencyPairEntity("1", "2", 2.0, "4/2/1999"),
                CurrencyPairEntity("3", "4", -1.0, "")
            ),
            viewModelScope
        )
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            CurrencyPairEntity("1", "2", 1.1, "1/1/2024"),
            CurrencyPairEntity("3", "4", 1.1, "1/1/2024")

        )
        assertEquals(
            listOf(
                DashboardItem.Base("1", "2", 1.1),
                DashboardItem.Base("3", "4", 1.1)
            ), actualList
        )
    }
}