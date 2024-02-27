package com.example.data.loadcurrencies

import com.example.data.core.ProvideResources
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.data.loadcurrencies.data.CurrenciesCacheDataSource
import com.example.domain.loadcurrencies.LoadCurrenciesRepository
import com.example.domain.loadcurrencies.LoadCurrenciesResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class BaseLoadCurrenciesRepositoryTest {

    private lateinit var repository: LoadCurrenciesRepository
    private lateinit var cloudDataSource: FakeCloudDataSource
    private lateinit var cacheDataSource: FakeCacheDataSource

    @Before
    fun setUp() {
        cloudDataSource = FakeCloudDataSource()
        cacheDataSource = FakeCacheDataSource()

        repository = BaseLoadCurrenciesRepository(
            cloudDataSource = cloudDataSource,
            cacheDataSource = cacheDataSource,
            provideResources = FakeProvideResources(),
        )
    }

    @Test
    fun testCacheIsEmptyAndDownloadCurrenciesFromCloud(): Unit = runBlocking {
        val loadingResult = repository.loadCurrencies()

        cacheDataSource.checkCurrenciesCalledCount(1)
        cloudDataSource.checkCurrenciesCalledCount(1)
        cacheDataSource.checkCurrencies(listOf("USD", "EUR"))
        assertEquals(LoadCurrenciesResult.Success, loadingResult)
    }

    @Test
    fun testCacheNotEmpty(): Unit = runBlocking {
        cacheDataSource.setCacheIsNotEmpty()

        val loadingResult = repository.loadCurrencies()

        cacheDataSource.checkCurrenciesCalledCount(1)
        cloudDataSource.checkCurrenciesCalledCount(0)
        assertEquals(LoadCurrenciesResult.Success, loadingResult)
    }

    @Test
    fun testNoCacheAndNoInternetConnection(): Unit = runBlocking {
        cloudDataSource.returnFailure(hasInternet = false)

        val loadingResult = repository.loadCurrencies()

        cacheDataSource.checkCurrenciesCalledCount(1)
        cloudDataSource.checkCurrenciesCalledCount(1)
        assertEquals(LoadCurrenciesResult.Error(message = "No internet connection"), loadingResult)
    }

    @Test
    fun testNoCacheAndServiceIsUnavailable(): Unit = runBlocking {
        cloudDataSource.returnFailure(hasInternet = true)

        val loadingResult = repository.loadCurrencies()

        cacheDataSource.checkCurrenciesCalledCount(1)
        cloudDataSource.checkCurrenciesCalledCount(1)
        assertEquals(LoadCurrenciesResult.Error(message = "Service unavailable"), loadingResult)
    }
}

private class FakeCloudDataSource : LoadCurrenciesCloudDataSource {

    private var returnSuccess = true
    private var isServiceUnavailable = true
    private var actualCurrenciesCalledCount = 0

    override fun currencies(): List<String> {
        actualCurrenciesCalledCount++
        return if (returnSuccess) {
            listOf("USD", "EUR")
        } else {
            throw if (isServiceUnavailable) RuntimeException() else UnknownHostException()
        }
    }

    fun checkCurrenciesCalledCount(expectedTimes: Int) {
        assertEquals(expectedTimes, actualCurrenciesCalledCount)
    }

    fun returnFailure(hasInternet: Boolean) {
        returnSuccess = false
        isServiceUnavailable = hasInternet
    }
}

private class FakeCacheDataSource : CurrenciesCacheDataSource {

    private var cacheIsEmpty = true
    private lateinit var cachedCurrencies: List<String>
    private var actualCurrenciesCalledCount = 0

    override suspend fun currencies(): List<String> {
        actualCurrenciesCalledCount++
        cachedCurrencies = if (cacheIsEmpty) emptyList<String>() else listOf("USD", "EUR")
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

private class FakeProvideResources() : ProvideResources {

    override fun noInternetConnectionMessage() = "No internet connection"

    override fun serviceUnavailableMessage() = "Service unavailable"
}