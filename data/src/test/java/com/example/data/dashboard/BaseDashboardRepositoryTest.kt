package com.example.data.dashboard

import com.example.data.core.ProvideResources
import com.example.data.dashboard.cache.CurrencyPairEntity
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
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
    private lateinit var provideResources: ProvideResources
    private lateinit var currentDate: CurrentDate

    @Before
    fun setUp() {
        favoriteCurrenciesCacheDataSource = FakeFavoriteCurrenciesCacheDataSource()
        currencyConverterCloudDataSource = FakeCurrencyConverterCloudDataSource()
        provideResources = FakeProvideResources()
        currentDate = FakeCurrentDate()
        repository = BaseDashboardRepository(
            favoriteCacheDataSource = favoriteCurrenciesCacheDataSource,
            provideResources = provideResources,
            mapper = DashboardItemMapper.Base(
                currencyConverterCloudDataSource,
                favoriteCurrenciesCacheDataSource,
                currentDate
            )
        )
    }


    @Test
    fun testFirstRun(): Unit = runBlocking {
        val result = repository.dashboards()
        assertEquals(DashboardResult.Empty, result)
    }


    @Test
    fun testUserHasValidCurrencyPairs(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasCache(actualData = true)

        val result = repository.dashboards()
        assertEquals(
            DashboardResult.Success(
                listOfItems = listOf(
                    DashboardItem.Base("A", "B", 2.0),
                    DashboardItem.Base("C", "D", 1.3)
                )
            ), result
        )
    }


    @Test
    fun testUserHasInValidCurrencyPairs(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasCache(actualData = false)

        val result = repository.dashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(
            listOf(CurrencyPairEntity("A", "B", 99.9, "1/1/2024"))
        )

        assertEquals(
            DashboardResult.Success(
                listOfItems = listOf(
                    DashboardItem.Base("A", "B", 99.9),
                    DashboardItem.Base("C", "D", 1.3)
                )
            ), result
        )
    }


    @Test
    fun testInvalidCurrencyPairAndNoInternet(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasCache(actualData = false)
        currencyConverterCloudDataSource.returnFailure(noConnection = true)

        val result = repository.dashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())

        assertEquals(
            DashboardResult.Error(message = "noInternetConnection"), result
        )
    }

    @Test
    fun testInvalidCurrencyPairAndServiceUnavailable(): Unit = runBlocking {
        favoriteCurrenciesCacheDataSource.hasCache(actualData = false)
        currencyConverterCloudDataSource.returnFailure(noConnection = false)

        val result = repository.dashboards()
        favoriteCurrenciesCacheDataSource.checkSavedCurrencyPairs(emptyList())

        assertEquals(
            DashboardResult.Error(message = "serviceUnavailable"), result
        )
    }

}

private class FakeFavoriteCurrenciesCacheDataSource : FavoriteCurrenciesCacheDataSource.Mutable {

    private var hasCache = false
    private var actualData = true

    override suspend fun favoriteCurrencies(): List<CurrencyPairEntity> {
        return if (hasCache) {
            val dates = if (actualData) {
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

    fun hasCache(actualData: Boolean) {
        hasCache = true
        this.actualData = actualData
    }

    private var savedCurrencyPairs = mutableListOf<CurrencyPairEntity>()

    override suspend fun save(currencyPair: CurrencyPairEntity) {
        savedCurrencyPairs.add(currencyPair)
    }

    fun checkSavedCurrencyPairs(pairs: List<CurrencyPairEntity>) {
        assertEquals(pairs, savedCurrencyPairs)
    }
}

private class FakeCurrencyConverterCloudDataSource : CurrencyConverterCloudDataSource {

    private var returnSuccess = true
    private var noConnection = true

    override suspend fun exchangeRate(from: String, to: String): Double {
        return if (returnSuccess) {
            99.9
        } else {
            throw if (noConnection) UnknownHostException() else RuntimeException()
        }
    }

    fun returnFailure(noConnection: Boolean) {
        returnSuccess = false
        this.noConnection = noConnection
    }
}

private class FakeProvideResources : ProvideResources {

    override fun noInternetConnectionMessage(): String {
        return "noInternetConnection"
    }

    override fun serviceUnavailableMessage(): String {
        return "serviceUnavailable"
    }
}

private class FakeCurrentDate : CurrentDate {

    override fun date() = "1/1/2024"
}