package com.example.presentation.dashboard

import org.junit.Assert.assertEquals
import org.junit.Test

class RatesFormatterTest {

    @Test
    fun test() {
        var ratesFormatter = RatesFormatter.Base()
        assertEquals("1.34", ratesFormatter.format(1.337))
        assertEquals("4.19", ratesFormatter.format(4.1923))
        assertEquals("423.1", ratesFormatter.format(423.1))

        ratesFormatter = RatesFormatter.Base(pattern = "#.#")
        assertEquals("1.3", ratesFormatter.format(1.337))
        assertEquals("4.2", ratesFormatter.format(4.1923))
        assertEquals("423.1", ratesFormatter.format(423.1))
    }
}