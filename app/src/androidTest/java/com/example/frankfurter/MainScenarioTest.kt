package com.example.frankfurter

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.main.MainActivity
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

/**
 * use android:name=".Mock" in AndroidManifest.xml
 */
@RunWith(AndroidJUnit4::class)
class MainScenarioTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainScenario() {
        val loadingPage = LoadingPage()
        loadingPage.checkVisible()
        loadingPage.checkError("No internet connection")
        activityScenarioRule.scenario.recreate()
        loadingPage.checkError("No internet connection")

        loadingPage.clickRetry()
        loadingPage.checkNotVisible()
        val dashboardPage = DashboardPage()
        dashboardPage.checkVisible()
        dashboardPage.checkNoAddedPairs()
        activityScenarioRule.scenario.recreate()
        dashboardPage.checkNoAddedPairs()

        dashboardPage.goToSettings()
        dashboardPage.checkNotVisible()
        val settingsPage = SettingsPage()
        settingsPage.checkVisible()
        settingsPage.checkFromCurrencies("USD", "EUR", "JPY")

        settingsPage.chooseFrom(position = 0)
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkToCurrencies("EUR", "JPY")
        activityScenarioRule.scenario.recreate()
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkToCurrencies("EUR", "JPY")

        settingsPage.chooseTo(position = 1)
        settingsPage.checkChosenTo(position = 1)
        activityScenarioRule.scenario.recreate()
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkToCurrencies("EUR", "JPY")
        settingsPage.checkChosenTo(position = 1)


        settingsPage.clickSave()
        dashboardPage.checkVisible()
        settingsPage.checkNotVisible()
        dashboardPage.checkPair(position = 0, currencyPair = "USD / JPY", rates = "10.1")

        dashboardPage.goToSettings()
        dashboardPage.checkNotVisible()
        settingsPage.checkVisible()
        settingsPage.checkFromCurrencies("USD", "EUR", "JPY")

        settingsPage.chooseFrom(position = 0)
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkToCurrencies("EUR")

        settingsPage.chooseTo(position = 0)
        settingsPage.checkChosenTo(position = 0)

        settingsPage.clickSave()
        settingsPage.checkNotVisible()
        dashboardPage.checkVisible()
        dashboardPage.checkPair(position = 0, currencyPair = "USD / JPY", rates = "10.1")
        dashboardPage.checkPair(position = 1, currencyPair = "USD / EUR", rates = "10.1")

        dashboardPage.goToSettings()
        dashboardPage.checkNotVisible()
        settingsPage.checkVisible()
        settingsPage.checkFromCurrencies("USD", "EUR", "JPY")

        Espresso.pressBack()
        settingsPage.checkNotVisible()
        dashboardPage.checkVisible()
        dashboardPage.checkPair(position = 0, currencyPair = "USD / JPY", rates = "10.1")
        dashboardPage.checkPair(position = 1, currencyPair = "USD / EUR", rates = "10.1")

        dashboardPage.goToSettings()
        dashboardPage.checkNotVisible()
        settingsPage.checkVisible()
        settingsPage.checkFromCurrencies("USD", "EUR", "JPY")

        settingsPage.chooseFrom(position = 0)
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkNoMoreCurrencies()
        activityScenarioRule.scenario.recreate()
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkNoMoreCurrencies()

        settingsPage.chooseFrom(position = 1)
        settingsPage.checkChosenFrom(position = 1)
        settingsPage.checkToCurrencies("USD", "JPY")

        settingsPage.goToDashboard()
        settingsPage.checkNotVisible()
        dashboardPage.checkVisible()
        dashboardPage.checkPair(position = 0, currencyPair = "USD / JPY", rates = "10.1")
        dashboardPage.checkPair(position = 1, currencyPair = "USD / EUR", rates = "10.1")

        dashboardPage.clickAtPair(position = 0)
        val deletePairPage = DeletePairPage()
        deletePairPage.checkVisible()
        activityScenarioRule.scenario.recreate()
        deletePairPage.checkVisible()

        deletePairPage.clickNo()
        deletePairPage.checkNotVisible()

        dashboardPage.checkPair(position = 0, currencyPair = "USD / JPY", rates = "10.1")
        dashboardPage.checkPair(position = 1, currencyPair = "USD / EUR", rates = "10.1")

        dashboardPage.clickAtPair(position = 0)
        deletePairPage.checkVisible()

        deletePairPage.clickYes()
        deletePairPage.checkNotVisible()
        dashboardPage.checkPair(position = 0, currencyPair = "USD / EUR", rates = "10.1")

        dashboardPage.goToSettings()
        dashboardPage.checkNotVisible()
        settingsPage.checkVisible()
        settingsPage.checkFromCurrencies("USD", "EUR", "JPY")

        settingsPage.chooseFrom(position = 0)
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkToCurrencies("JPY")

        settingsPage.goToDashboard()
        settingsPage.checkNotVisible()
        dashboardPage.checkVisible()
        dashboardPage.checkPair(position = 0, currencyPair = "USD / EUR", rates = "10.1")

        dashboardPage.clickAtPair(position = 0)
        deletePairPage.checkVisible()
        activityScenarioRule.scenario.recreate()
        deletePairPage.checkVisible()

        deletePairPage.clickYes()
        deletePairPage.checkNotVisible()
        dashboardPage.checkNoAddedPairs()

        dashboardPage.goToSettings()
        dashboardPage.checkNotVisible()
        settingsPage.checkVisible()
        settingsPage.checkFromCurrencies("USD", "EUR", "JPY")

        settingsPage.chooseFrom(position = 0)
        settingsPage.checkChosenFrom(position = 0)
        settingsPage.checkToCurrencies("EUR", "JPY")
    }
}