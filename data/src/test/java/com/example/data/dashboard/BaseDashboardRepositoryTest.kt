package com.example.data.dashboard

import com.example.data.FakeHandleError
import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.core.FakeFavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.domain.dashboard.ForegroundDownloadWorkManagerWrapper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class BaseDashboardRepositoryTest {

    private lateinit var repository: DashboardRepository
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var cacheDataSource: FakeCacheDataSource
    private lateinit var favoriteCurrenciesCacheDataSource: FakeFavoriteCurrenciesCacheDataSource
    private lateinit var dashboardItemsDatasource: FakeDashboardItemsDatasource
    private lateinit var handleError: FakeHandleError
    private lateinit var foregroundWrapper: FakeForegroundDownloadWorkManagerWrapper

    @Before
    fun setUp() {
        cloudDataSource = FakeCloudDataSource()
        cacheDataSource = FakeCacheDataSource()
        favoriteCurrenciesCacheDataSource = FakeFavoriteCurrenciesCacheDataSource()
        dashboardItemsDatasource = FakeDashboardItemsDatasource()
        handleError = FakeHandleError()
        foregroundWrapper = FakeForegroundDownloadWorkManagerWrapper()
        repository = BaseDashboardRepository(
            cloudDataSource = cloudDataSource,
            cacheDataSource = cacheDataSource,
            favoriteCacheDataSource = favoriteCurrenciesCacheDataSource,
            dashboardItemsDatasource = dashboardItemsDatasource,
            handleError = handleError,
            foregroundWrapper = foregroundWrapper
        )
    }

    @Test
    fun testNoCategoriesAndSavedPairsInCache(): Unit = runBlocking {
        val result = repository.dashboards()
        cacheDataSource.checkCurrenciesCalledCount(1)
        cloudDataSource.checkCurrenciesCalledCount(1)
        cacheDataSource.checkCurrencies(listOf("A", "B", "C", "D"))
        foregroundWrapper.checkStartCalledCount(0)
        assertEquals(DashboardResult.Empty, result)
    }


    @Test
    fun testNoCategoriesAndSavedPairsInCacheButLoadCategoriesFailed(): Unit = runBlocking {
        cloudDataSource.loadFailed()

        val result = repository.dashboards()
        cacheDataSource.checkCurrenciesCalledCount(1)
        cloudDataSource.checkCurrenciesCalledCount(1)
        cacheDataSource.checkCurrencies(listOf())
        foregroundWrapper.checkStartCalledCount(0)

        assertEquals(
            DashboardResult.Error(message = UnknownHostException::class.java.simpleName), result
        )
    }

    @Test
    fun testHasCachedCategoriesButNoSavedPairsInCache(): Unit = runBlocking {
        cacheDataSource.setCacheIsNotEmpty()

        val result = repository.dashboards()
        cacheDataSource.checkCurrenciesCalledCount(1)
        cloudDataSource.checkCurrenciesCalledCount(0)
        cacheDataSource.checkCurrencies(listOf("A", "B", "C", "D"))
        foregroundWrapper.checkStartCalledCount(0)
        assertEquals(DashboardResult.Empty, result)
    }

    @Test
    fun testUserHasSavedPairs(): Unit = runBlocking {
        cacheDataSource.setCacheIsNotEmpty()
        favoriteCurrenciesCacheDataSource.hasValidCache()
        dashboardItemsDatasource.doNotNeedToDownloadData()

        val actualResult = repository.dashboards()
        foregroundWrapper.checkStartCalledCount(0)
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
    fun testInvalidCurrencyPairAndFail(): Unit = runBlocking {
        cacheDataSource.setCacheIsNotEmpty()
        favoriteCurrenciesCacheDataSource.hasInvalidCache()
        dashboardItemsDatasource.returnFail()

        var actualResult = repository.dashboards()
        foregroundWrapper.checkStartCalledCount(1)
        assertEquals(DashboardResult.NoDataYet, actualResult)

        actualResult = repository.downloadDashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            CurrencyPairEntity("A", "B", 2.0, "15/3/2020"),
            CurrencyPairEntity("C", "D", 1.3, "1/1/2024")
        )
        assertEquals(
            DashboardResult.Error(message = IllegalStateException::class.simpleName!!), actualResult
        )
    }

    @Test
    fun testInvalidCurrencyPairAndSuccess(): Unit = runBlocking {
        cacheDataSource.setCacheIsNotEmpty()
        favoriteCurrenciesCacheDataSource.hasInvalidCache()

        var actualResult = repository.dashboards()
        foregroundWrapper.checkStartCalledCount(1)
        assertEquals(DashboardResult.NoDataYet, actualResult)

        actualResult = repository.downloadDashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            CurrencyPairEntity("A", "B", 2.0, "15/3/2020"),
            CurrencyPairEntity("C", "D", 1.3, "1/1/2024")
        )
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
    fun testRemovePair(): Unit = runBlocking {
        cacheDataSource.setCacheIsNotEmpty()
        favoriteCurrenciesCacheDataSource.hasValidCache()

        repository.removePair(from = "A", to = "B")
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            CurrencyPairEntity("C", "D", 1.3, "1/1/2024")
        )
    }
}

private class FakeCloudDataSource : LoadCurrenciesCloudDataSource {

    private var returnSuccess = true
    private var actualCurrenciesCalledCount = 0

    override fun currencies(): List<String> {
        actualCurrenciesCalledCount++
        return if (returnSuccess) {
            listOf("A", "B", "C", "D")
        } else {
            throw UnknownHostException()
        }
    }

    fun checkCurrenciesCalledCount(expectedTimes: Int) {
        assertEquals(expectedTimes, actualCurrenciesCalledCount)
    }

    fun loadFailed() {
        returnSuccess = false
    }
}

private class FakeCacheDataSource : CurrenciesCacheDataSource.Mutable {

    private var cacheIsEmpty = true
    private lateinit var cachedCurrencies: List<String>
    private var actualCurrenciesCalledCount = 0

    override suspend fun currencies(): List<String> {
        actualCurrenciesCalledCount++
        cachedCurrencies = if (cacheIsEmpty) emptyList() else listOf("A", "B", "C", "D")
        return cachedCurrencies
    }

    fun setCacheIsNotEmpty() {
        cacheIsEmpty = false
    }

    fun checkCurrenciesCalledCount(expectedTimes: Int) {
        assertEquals(expectedTimes, actualCurrenciesCalledCount)
    }

    override suspend fun saveCurrencies(currencies: List<String>) {
        cachedCurrencies = currencies
    }

    fun checkCurrencies(expectedCurrencies: List<String>) {
        assertEquals(expectedCurrencies, cachedCurrencies)
    }
}

private class FakeDashboardItemsDatasource : DashboardItemsDatasource {

    private var returnSuccess = true

    override suspend fun dashboardItems(favoriteCurrencies: List<CurrencyPairEntity>) =
        if (returnSuccess) {
            favoriteCurrencies.map { DashboardItem.Base(it.fromCurrency, it.toCurrency, it.rates) }
        } else {
            throw IllegalStateException()
        }

    fun returnFail() {
        returnSuccess = false
    }

    private var needToDownloadData = true

    override suspend fun needToDownloadData(favoriteCurrencies: List<CurrencyPairEntity>): Boolean {
        return needToDownloadData
    }

    fun doNotNeedToDownloadData() {
        needToDownloadData = false
    }
}

private class FakeForegroundDownloadWorkManagerWrapper : ForegroundDownloadWorkManagerWrapper {

    private var actualStartCalledCount = 0

    override fun start() {
        actualStartCalledCount++
    }

    fun checkStartCalledCount(expectedTimes: Int) {
        assertEquals(expectedTimes, actualStartCalledCount)
    }
}