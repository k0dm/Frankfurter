package com.example.presentation.main

import androidx.lifecycle.LiveData
import com.example.presentation.loadingcurrencies.LoadingCurrenciesScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var navigation: FakeNavigation

    @Before
    fun setUp() {
        navigation = FakeNavigation()
        viewModel = MainViewModel(navigation = navigation)
    }

    @Test
    fun testFirstRun() {
        viewModel.init(isFirstRun = true)
        navigation.checkScreen(LoadingCurrenciesScreen)
    }

    @Test
    fun testNotFirstRun() {
        viewModel.init(isFirstRun = false)
        navigation.checkScreen(Screen.Empty)
    }
}

private class FakeNavigation: Navigation.Mutable {

    private var actualScreen: Screen = Screen.Empty

    override fun updateUi(value: Screen) {
        actualScreen =  value
    }

    fun checkScreen(expectedScreen: Screen) {
        assertEquals(expectedScreen, actualScreen)
    }

    override fun liveData(): LiveData<Screen> {
        throw IllegalStateException("don't use in UnitTest")
    }
}