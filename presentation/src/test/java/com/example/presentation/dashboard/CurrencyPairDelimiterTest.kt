package com.example.presentation.dashboard

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyPairDelimiterTest {

    @Test
    fun test() {
        val currencyPairDelimiter = CurrencyPairDelimiter.Base(currencyPairDelimiter = "+")

        val actualResult = currencyPairDelimiter.addDelimiter(from = "USD", to = "EUR")
        assertEquals("USD+EUR", actualResult)

        val actualScreen = currencyPairDelimiter.makeDeletePairScreen("JPY+GPG")
        assertEquals(DeletePairScreen(fromCurrency = "JPY", toCurrency = "GPG"), actualScreen)
    }
}