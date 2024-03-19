package com.example.data.dashboard.cloud

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyConverterCloudDataSourceTest {


    private lateinit var cloudDataSource: CurrencyConverterCloudDataSource

    @Before
    fun setUp() {
        val service = Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyConverterService::class.java)
        cloudDataSource = CurrencyConverterCloudDataSource.Base(service = service)
    }

    /**
     * https://api.frankfurter.app/latest?from=GBP&to=USD
     *
     * {
     *     "amount": 1.0,
     *     "base": "GBP",
     *     "date": "2024-02-28",
     *     "rates": {
     *         "USD": 1.2634
     *     }
     * }
     */
    @Test
    fun test() = runBlocking {
        val actualResult = cloudDataSource.exchangeRate("GBP", "USD")
        assertEquals(1.2634, actualResult, 0.1)
    }
}