package com.example.data.dashboard

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.core.FakeCurrencyConverterCloudDataSource
import com.example.data.dashboard.core.FakeCurrentDate
import com.example.data.dashboard.core.FakeFavoriteCurrenciesCacheDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class BaseDashboardRepositoryTest {

    private lateinit var repository: DashboardRepository
    private lateinit var favoriteCurrenciesCacheDataSource: FakeFavoriteCurrenciesCacheDataSource
    private lateinit var currencyConverterCloudDataSource: FakeCurrencyConverterCloudDataSource
    private lateinit var handleError: FakeHandleError
    private lateinit var currentDate: FakeCurrentDate

    @Before
    fun setUp() {
        favoriteCurrenciesCacheDataSource = FakeFavoriteCurrenciesCacheDataSource()
        currencyConverterCloudDataSource = FakeCurrencyConverterCloudDataSource()
        handleError = FakeHandleError()
        currentDate = FakeCurrentDate("1/1/2024")
        repository = BaseDashboardRepository(
            favoriteCacheDataSource = favoriteCurrenciesCacheDataSource,
            mapper = DashboardItemMapper.Base(
                currencyConverterCloudDataSource,
                favoriteCurrenciesCacheDataSource,
                currentDate
            ),
            handleError = handleError
        )
    }

    @Test
    fun testEmptyCache(): Unit = runBlocking {
        val result = repository.dashboards()
        assertEquals(DashboardResult.Empty, result)
    }

    @Test
    fun testUserHasValidCurrencyPairs(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasValidCache()

        val actualResult = repository.dashboards()
        assertEquals(
            DashboardResult.Success(
                listOfItems = listOf(
                    DashboardItem.Base("A", "B", 2.0),
                    DashboardItem.Base("C", "D", 1.3)
                )
            ), actualResult
        )
    }

    @Test
    fun testUserHasInvalidCurrencyPairs(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasInvalidCache()

        val actualResult = repository.dashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            listOf(CurrencyPairEntity("A", "B", 99.9, "1/1/2024"))
        )

        assertEquals(
            DashboardResult.Success(
                listOfItems = listOf(
                    DashboardItem.Base("A", "B", 99.9),
                    DashboardItem.Base("C", "D", 1.3)
                )
            ), actualResult
        )
    }

    @Test
    fun testInvalidCurrencyPairAndNoInternet(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasInvalidCache()
        currencyConverterCloudDataSource.returnFailureNoInternet()

        val result = repository.dashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())

        assertEquals(
            DashboardResult.Error(message = "noInternetConnection"), result
        )
    }

    @Test
    fun testInvalidCurrencyPairAndServiceUnavailable(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasInvalidCache()
        currencyConverterCloudDataSource.returnFailureServiceUnavailable()

        val actualResult = repository.dashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())

        assertEquals(
            DashboardResult.Error(message = "serviceUnavailable"), actualResult
        )
    }
}

private class FakeHandleError : HandleError {

    override fun handle(e: Exception) =
        if (e is UnknownHostException) "noInternetConnection" else "serviceUnavailable"
}