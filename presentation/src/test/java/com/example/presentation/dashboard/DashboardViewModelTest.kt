package com.example.presentation.dashboard

import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
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
    private lateinit var mapper: FakeMapper

    @Before
    fun setUp() {
        navigation = FakeNavigation()
        dashboardCommunication = FakeDashboardCommunication()
        dashboardRepository = FakeDashboardRepository()
        mapper = FakeMapper(communication = dashboardCommunication)
        runAsync = FakeRunAsync()
        viewModel = DashboardViewModel(
            navigation = navigation,
            communication = dashboardCommunication,
            repository = dashboardRepository,
            runAsync = runAsync,
            currencyPairDelimiter = FakeCurrencyPairDelimiter(),
            mapper = mapper
        )
    }

    @Test
    fun testNoData() {
        viewModel.init()
        dashboardCommunication.checkUiState(DashboardUiState.Progress)

        runAsync.pingResult()
        dashboardCommunication.checkUiState(DashboardUiState.Progress, DashboardUiState.Empty)
    }

    @Test
    fun testPairsAvailable() {
        dashboardRepository.returnSuccess()

        viewModel.init()
        dashboardCommunication.checkUiState(DashboardUiState.Progress)

        runAsync.pingResult()
        dashboardCommunication.checkUiState(
            DashboardUiState.Progress, DashboardUiState.Success(
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
        dashboardCommunication.checkUiState(
            DashboardUiState.Progress,
            DashboardUiState.Error(message = "No internet connection")
        )

        dashboardRepository.returnSuccess()

        viewModel.retry()

        runAsync.pingResult()
        dashboardCommunication.checkUiState(
            DashboardUiState.Progress,
            DashboardUiState.Error(message = "No internet connection"),
            DashboardUiState.Progress,
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
        navigation.checkScreen(SettingsScreen)
    }

    @Test
    fun testOpenDeletePairDialog() {
        viewModel.openDeletePairDialog(currencyPair = "A / B")
        navigation.checkScreen(DeletePairScreen(fromCurrency = "A", toCurrency = "B"))
    }

    @Test
    fun testRemovePair() {
        viewModel.removePair(from = "A", to = "B")

        dashboardRepository.checkedRemovedPair("A", "B")
        runAsync.pingResult()
        dashboardCommunication.checkUiState(
            DashboardUiState.Success(
                currencies = listOf(
                    DashboardCurrencyPairUi.Base("C / D", "2.11")
                )
            )
        )
    }
}

private class FakeDashboardCommunication : DashboardCommunication {

    private val actualUiStateList: MutableList<DashboardUiState> = mutableListOf()

    override fun updateUi(value: DashboardUiState) {
        actualUiStateList.add(value)
    }

    fun checkUiState(vararg expectedUiState: DashboardUiState) {
        assertEquals(expectedUiState.toList(), actualUiStateList)
    }
}

private class FakeDashboardRepository : DashboardRepository {

    private var dashboardResult: DashboardResult = DashboardResult.Empty

    override suspend fun dashboards(): DashboardResult {
        return dashboardResult
    }

    override suspend fun downloadDashboards(): DashboardResult {
        return DashboardResult.NoDataYet
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

    private var removedPair = Pair("", "")

    override suspend fun removePair(from: String, to: String): DashboardResult {
        removedPair = Pair(from, to)
        dashboardResult = DashboardResult.Success(
            listOfItems = listOf(
                if (from == "C") DashboardItem.Base("A", "B", 1.457)
                else DashboardItem.Base("C", "D", 2.1132),
            )
        )
        return dashboards()
    }

    fun checkedRemovedPair(from: String, to: String) {
        assertEquals(Pair(from, to), removedPair)
    }
}

private class FakeCurrencyPairDelimiter(
    private val delimeter: String = " / "
) : CurrencyPairDelimiter.Mutable {

    override fun makeDeletePairScreen(currencyPair: String): DeletePairScreen =
        currencyPair.split(delimeter).let { DeletePairScreen(it[0], it[1]) }


    override fun addDelimiter(from: String, to: String): String = "$from$delimeter$to"
}

private class FakeMapper(
    private val communication: FakeDashboardCommunication,
    private val mapper: DashboardItem.Mapper<DashboardCurrencyPairUi> = BaseDashboardItemMapper(
        FakeCurrencyPairDelimiter()
    )
) : DashboardResult.Mapper {

    override fun mapSuccess(listOfItems: List<DashboardItem>) {
        communication.updateUi(DashboardUiState.Success(currencies = listOfItems.map { it.map(mapper) }))
    }

    override fun mapError(message: String) {
        communication.updateUi(DashboardUiState.Error(message = message))
    }

    override fun mapEmpty() {
        communication.updateUi(DashboardUiState.Empty)
    }
}