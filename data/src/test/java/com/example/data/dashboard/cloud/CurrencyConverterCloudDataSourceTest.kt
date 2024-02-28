package com.example.data.dashboard.cloud

import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okio.Timeout
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyConverterCloudDataSourceTest {


    private lateinit var cloudDataSource: CurrencyConverterCloudDataSource

    @Before
    fun setUp() {
        cloudDataSource = CurrencyConverterCloudDataSource.Base(service = FakeService())
    }

    @Test
    fun test() = runBlocking {
        val actualResult = cloudDataSource.exchangeRate("A", "B")
        assertEquals(1.3, actualResult, 0.1)
    }
}

private class FakeService : CurrencyConverterService {

    override fun exchangeRate(from: String, to: String) = object : Call<CurrencyRatesCloud> {

        override fun execute() = Response.success(
            CurrencyRatesCloud(rates = HashMap<String, Double>().apply {
                put("B", 1.3)
            })
        )

        override fun clone(): Call<CurrencyRatesCloud> = TODO("Not yet implemented")
        override fun isExecuted(): Boolean = false
        override fun cancel() = Unit
        override fun isCanceled() = false
        override fun request(): Request = TODO("Not yet implemented")
        override fun timeout(): Timeout = TODO("Not yet implemented")
        override fun enqueue(callback: Callback<CurrencyRatesCloud>) = Unit
    }
}