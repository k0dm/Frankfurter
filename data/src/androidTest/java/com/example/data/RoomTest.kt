package com.example.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.core.CurrenciesDatabase
import com.example.data.loadcurrencies.cache.CurrenciesDao
import com.example.data.loadcurrencies.cache.CurrencyEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var db: CurrenciesDatabase
    private lateinit var dao: CurrenciesDao

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(
            context, CurrenciesDatabase::class.java
        )
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        dao = db.currenciesDao()
    }

    @After
    @Throws(IOException::class)
    fun clear() {
        db.close()
    }

    @Test
    fun test() = runBlocking {
        assertEquals(emptyList<CurrencyEntity>(), dao.currencies())

        dao.saveCurrencies(listOf(CurrencyEntity("EUR"), CurrencyEntity("USD")))

        var expected = listOf(CurrencyEntity("EUR"), CurrencyEntity("USD"))
        assertEquals(expected, dao.currencies())

        dao.saveCurrencies(listOf(CurrencyEntity("UAH")))
        expected = listOf(
            CurrencyEntity("EUR"),
            CurrencyEntity("USD"),
            CurrencyEntity("UAH")
        )
        assertEquals(expected, dao.currencies())
    }
}