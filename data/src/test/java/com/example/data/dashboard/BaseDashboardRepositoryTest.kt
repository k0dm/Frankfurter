package com.example.data.dashboard

import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.core.FakeFavoriteCurrenciesCacheDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BaseDashboardRepositoryTest {

    private lateinit var repository: DashboardRepository
    private lateinit var favoriteCurrenciesCacheDataSource: FakeFavoriteCurrenciesCacheDataSource
    private lateinit var dashboardItemsDatasource: FakeDashboardItemsDatasource
    private lateinit var handleError: FakeHandleError

    @Before
    fun setUp() {
        favoriteCurrenciesCacheDataSource = FakeFavoriteCurrenciesCacheDataSource()
        dashboardItemsDatasource = FakeDashboardItemsDatasource()
        handleError = FakeHandleError()
        repository = BaseDashboardRepository(
            favoriteCacheDataSource = favoriteCurrenciesCacheDataSource,
            dashboardItemsDatasource = dashboardItemsDatasource,
            handleError = handleError
        )
    }

    @Test
    fun testEmptyCache(): Unit = runBlocking {
        val result = repository.dashboards()
        assertEquals(DashboardResult.Empty, result)
    }

    @Test
    fun testUserHasCache(): Unit = runBlocking {
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
    fun testInvalidCurrencyPairAndNoInternet(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasInvalidCache()
        dashboardItemsDatasource.returnFail()

        val result = repository.dashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())

        assertEquals(
            DashboardResult.Error(message = IllegalStateException::class.simpleName!!), result
        )
    }
}

private class FakeDashboardItemsDatasource : DashboardItemsDatasource {

    private var returnSuccess = true

    override suspend fun map(favoriteCurrencies: List<CurrencyPairEntity>) = if (returnSuccess) {
        favoriteCurrencies.map { DashboardItem.Base(it.fromCurrency, it.toCurrency, it.rates) }
    } else {
        throw IllegalStateException()
    }

    fun returnFail() {
        returnSuccess = false
    }
}

private class FakeHandleError : HandleError {

    override fun handle(e: Exception): String = e.javaClass.simpleName
}