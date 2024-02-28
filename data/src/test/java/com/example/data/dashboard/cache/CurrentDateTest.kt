package com.example.data.dashboard.cache

import org.junit.Assert.assertEquals
import org.junit.Test

class CurrentDateTest {

    @Test
    fun test() {
        var currentDate = CurrentDate.Base(format = "yyyy")
        assertEquals(4, currentDate.date().length)

        currentDate = CurrentDate.Base(format = "yyyy/MM")
        assertEquals(7, currentDate.date().length)

        currentDate = CurrentDate.Base(format = "yyyy/MM/dd")
        assertEquals(10, currentDate.date().length)
    }
}
