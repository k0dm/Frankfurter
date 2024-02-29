package com.example.presentation.core

import com.example.presentation.main.Navigation
import com.example.presentation.main.Screen
import org.junit.Assert

internal class FakeNavigation: Navigation.Mutable, FakeUpdateNavigation {

    private var actualScreen: Screen = Screen.Empty

    override fun updateUi(value: Screen) {
        actualScreen =  value
    }

    override fun checkScreen(expectedScreen: Screen) {
        Assert.assertEquals(expectedScreen, actualScreen)
    }
}

internal interface FakeUpdateNavigation : Navigation.Update {

    fun checkScreen(expectedScreen: Screen)
}