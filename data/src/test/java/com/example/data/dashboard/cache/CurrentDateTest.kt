package com.example.data.dashboard.cache

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class CurrentDateTest {

    @Test
    fun test() {
        var currentDate =
            CurrentDate.Base(date = LocalDateTime.of(2020, 11, 25, 11, 11), format = "yyyy")
        assertEquals("2020", currentDate.date())

        currentDate =
            CurrentDate.Base(date = LocalDateTime.of(2020, 11, 25, 11, 11), format = "yyyy/MM")
        assertEquals("2020/11", currentDate.date())

        currentDate =
            CurrentDate.Base(date = LocalDateTime.of(2020, 11, 25, 11, 11), format = "yyyy/MM/dd")
        assertEquals("2020/11/25", currentDate.date())

        currentDate =
            CurrentDate.Base(
                date = LocalDateTime.of(2020, 11, 25, 11, 11),
                format = "yyyy/MM/dd HH"
            )
        assertEquals("2020/11/25 11", currentDate.date())

        currentDate =
            CurrentDate.Base(
                date = LocalDateTime.of(2020, 11, 25, 11, 11),
                format = "yyyy/MM/dd HH:mm"
            )
        assertEquals("2020/11/25 11:11", currentDate.date())

        currentDate =
            CurrentDate.Base(
                date = LocalDateTime.of(2020, 11, 25, 11, 11, 33),
                format = "yyyy/MM/dd HH:mm:ss"
            )
        assertEquals("2020/11/25 11:11:33", currentDate.date())
    }
}
