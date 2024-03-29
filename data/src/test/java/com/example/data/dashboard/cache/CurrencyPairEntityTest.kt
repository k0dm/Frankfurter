package com.example.data.dashboard.cache

import com.example.data.dashboard.core.FakeCurrentDate
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyPairEntityTest {

    private val currentDate = FakeCurrentDate("28/02/2024")

    @Test
    fun testValidRate() {
        val currencyPairEntity = CurrencyPairEntity("1", "2", 1.0, "28/02/2024")
        assertEquals(false, currencyPairEntity.isInvalidRate(currentDate))
    }

    @Test
    fun testInvalidRate() {
        val currencyPairEntity = CurrencyPairEntity("1", "2", -1.0, "28/02/2024")
        assertEquals(true, currencyPairEntity.isInvalidRate(currentDate))
    }

    @Test
    fun testInvalidDate() {
        val currencyPairEntity = CurrencyPairEntity("1", "2", 1.0, "27/02/2024")
        assertEquals(true, currencyPairEntity.isInvalidRate(currentDate))
    }

    @Test
    fun testInvalidRateAndDate() {
        val currencyPairEntity = CurrencyPairEntity("1", "2", -1.0, "27/02/2024")
        assertEquals(true, currencyPairEntity.isInvalidRate(currentDate))
    }
}