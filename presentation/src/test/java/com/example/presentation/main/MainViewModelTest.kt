package com.example.presentation.main

import com.example.presentation.core.FakeNavigation
import com.example.presentation.dashboard.DashboardScreen
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
        navigation.checkScreen(DashboardScreen)
    }

    @Test
    fun testNotFirstRun() {
        viewModel.init(isFirstRun = false)
        navigation.checkScreen(Screen.Empty)
    }
}

