package com.example.presentation.dashboard

import org.junit.Assert.assertEquals
import org.junit.Test

class ConcurrencyPairDelimiterTest {

    @Test
    fun test() {
        val concurrencyPairDelimiter = ConcurrencyPairDelimiter.Base(concurrencyPairDelimiter = "+")

        val actualResult = concurrencyPairDelimiter.addDelimiter(from = "USD", to = "EUR")
        assertEquals("USD+EUR", actualResult)

        val actualScreen = concurrencyPairDelimiter.makeDeletePairScreen("JPY+GPG")
        assertEquals(DeletePairScreen(fromCurrency = "JPY", toCurrency = "GPG"), actualScreen)
    }
}