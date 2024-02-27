package com.example.presentation.loadingcurrencies

import androidx.lifecycle.LiveData
import com.example.domain.LoadCurrenciesRepository
import com.example.domain.LoadCurrenciesResult
import com.example.presentation.core.FakeClear
import com.example.presentation.core.FakeNavigation
import com.example.presentation.core.FakeRunAsync
import com.example.presentation.core.FakeUpdateNavigation
import com.example.presentation.dashboard.DashboardScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoadingCurrenciesViewModelTest {

    private lateinit var viewModel: LoadingCurrenciesViewModel
    private lateinit var navigation: FakeUpdateNavigation
    private lateinit var communication: FakeCommunication
    private lateinit var repository: FakeRepositoryLoad
    private lateinit var runAsync: FakeRunAsync
    private lateinit var clear: FakeClear

    @Before
    fun setUp(){
        navigation = FakeNavigation()
        communication = FakeCommunication()
        repository = FakeRepositoryLoad()
        runAsync = FakeRunAsync()
        clear = FakeClear()
        viewModel = LoadingCurrenciesViewModel(
            navigation = navigation,
            communication = communication,
            repository = repository,
            runAsync = runAsync,
            clearViewModel = clear
        )
    }

    @Test
    fun testFirstRunAndSuccess() {
        viewModel.init(true)
        communication.checkUiState(LoadingCurrenciesUiState.Loading)

        runAsync.pingResult()
        repository.checkLoadCurrenciesCalledCount(1)
        clear.checkClearCalled(listOf(LoadingCurrenciesViewModel::class.java))
        navigation.updateUi(DashboardScreen)
    }

    @Test
    fun testNotFirstRun(){
        repository.hasCacheCurrencies()

        viewModel.init(false)
        repository.checkLoadCurrenciesCalledCount(0)
        clear.checkClearCalled(emptyList())
    }

    @Test
    fun testFirstRunAndError() {
        repository.loadSuccess = false

        viewModel.init(true)
        communication.checkUiState(LoadingCurrenciesUiState.Loading)

        runAsync.pingResult()
        communication.checkUiState(LoadingCurrenciesUiState.Error(message = "Service unavailable"))

        viewModel.loadCurrencies()
        communication.checkUiState(LoadingCurrenciesUiState.Loading)

        runAsync.pingResult()
        communication.checkUiState(LoadingCurrenciesUiState.Error(message = "Service unavailable"))
    }
}

private class FakeRepositoryLoad : LoadCurrenciesRepository {

    var loadSuccess = true

    private var actualCurrencies = emptyList<String>()

    fun hasCacheCurrencies() {
        actualCurrencies = listOf("USD", "EUR", "UAH")
    }

    private var loadCurrenciesCalledCount = 0

    override suspend fun loadCurrencies(): LoadCurrenciesResult {

        loadCurrenciesCalledCount++

        return if (loadSuccess) {
            actualCurrencies = listOf("USD", "EUR", "UAH")
            LoadCurrenciesResult.Success
        } else {
            LoadCurrenciesResult.Error("Service unavailable")
        }
    }

    fun checkLoadCurrenciesCalledCount(expectedCount: Int) {
        assertEquals(expectedCount, loadCurrenciesCalledCount)
    }
}

private class FakeCommunication: LoadingCurrenciesCommunication {

    private var actualUiState: LoadingCurrenciesUiState = LoadingCurrenciesUiState.Empty

    override fun updateUi(value: LoadingCurrenciesUiState) {
        actualUiState = value
    }

    fun checkUiState(expectedUiState: LoadingCurrenciesUiState) {
        assertEquals(expectedUiState, actualUiState)
    }

    override fun liveData(): LiveData<LoadingCurrenciesUiState> =
        throw IllegalStateException("don't use in UnitTest")
}