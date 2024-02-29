package com.example.presentation.dashboard

import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.presentation.core.FakeClear
import com.example.presentation.core.FakeNavigation
import com.example.presentation.core.FakeRunAsync
import com.example.presentation.core.FakeUpdateNavigation
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi
import com.example.presentation.settings.SettingsScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var navigation: FakeUpdateNavigation
    private lateinit var dashboardCommunication: FakeDashboardCommunication
    private lateinit var dashboardRepository: FakeDashboardRepository
    private lateinit var runAsync: FakeRunAsync
    private lateinit var clearViewModel: FakeClear

    @Before
    fun setUp() {
        navigation = FakeNavigation()
        dashboardCommunication = FakeDashboardCommunication()
        dashboardRepository = FakeDashboardRepository()
        runAsync = FakeRunAsync()
        clearViewModel = FakeClear()
        viewModel = DashboardViewModel(
            navigation = navigation,
            communication = dashboardCommunication,
            repository = dashboardRepository,
            runAsync = runAsync,
            clearViewModel = clearViewModel
        )
    }

    @Test
    fun testNoData() {
        viewModel.init()
        dashboardCommunication.checkUiState(DashboardUiState.Progress)

        runAsync.pingResult()
        dashboardCommunication.checkUiState(DashboardUiState.Empty)
    }

    @Test
    fun testPairsAvailable() {
        dashboardRepository.returnSuccess()

        viewModel.init()
        dashboardCommunication.checkUiState(DashboardUiState.Progress)

        runAsync.pingResult()
        dashboardCommunication.checkUiState(
            DashboardUiState.Success(
                currencies = listOf(
                    DashboardCurrencyPairUi.Base("A / B", "1.46"),
                    DashboardCurrencyPairUi.Base("C / D", "2.11")
                )
            )
        )
    }

    @Test
    fun failedToLoadPairsAndRatesThenSuccess() {
        dashboardRepository.returnError()

        viewModel.init()
        dashboardCommunication.checkUiState(DashboardUiState.Progress)

        runAsync.pingResult()
        dashboardCommunication.checkUiState(DashboardUiState.Error(message = "No internet connection"))

        dashboardRepository.returnSuccess()

        viewModel.retry()
        dashboardCommunication.checkUiState(DashboardUiState.Progress)

        runAsync.pingResult()
        dashboardCommunication.checkUiState(
            DashboardUiState.Success(
                currencies = listOf(
                    DashboardCurrencyPairUi.Base("A / B", "1.46"),
                    DashboardCurrencyPairUi.Base("C / D", "2.11")
                )
            )
        )
    }

    @Test
    fun testNavigateToSettings() {
        viewModel.goToSettings()
        clearViewModel.checkClearCalled(listOf(DashboardViewModel::class.java))
        navigation.checkScreen(SettingsScreen)
    }
}

private class FakeDashboardCommunication : DashboardCommunication {

    private var actualUiState: DashboardUiState = DashboardUiState.Empty

    override fun updateUi(value: DashboardUiState) {
        actualUiState = value
    }

    fun checkUiState(expectedUiState: DashboardUiState) {
        assertEquals(expectedUiState, actualUiState)
    }
}

private class FakeDashboardRepository : DashboardRepository {

    private var dashboardResult: DashboardResult = DashboardResult.Empty

    override suspend fun dashboards(): DashboardResult {
        return dashboardResult
    }

    fun returnSuccess() {
        dashboardResult = DashboardResult.Success(
            listOfItems = listOf(
                DashboardItem.Base("A", "B", 1.457),
                DashboardItem.Base("C", "D", 2.1132),
            )
        )
    }

    fun returnError() {
        dashboardResult = DashboardResult.Error(message = "No internet connection")
    }
}