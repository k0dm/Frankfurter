package com.example.presentation.loadingcurrencies

import androidx.lifecycle.LiveData
import com.example.domain.CurrenciesRepository
import com.example.domain.LoadCurrenciesResult
import com.example.presentation.core.FakeNavigation
import com.example.presentation.core.FakeRunAsync
import com.example.presentation.core.FakeUpdateNavigation
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoadingCurrenciesViewModelTest {

    private lateinit var viewModel: LoadingCurrenciesViewModel
    private lateinit var navigation: FakeUpdateNavigation
    private lateinit var communication: FakeCommunication
    private lateinit var repository: FakeRepository
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun setUp(){
        navigation = FakeNavigation()
        communication = FakeCommunication()
        repository = FakeRepository()
        runAsync = FakeRunAsync()

        viewModel = LoadingCurrenciesViewModel(
            navigation = navigation,
            communication = communication,
            repository = repository,
            runAsync = runAsync
        )
    }

    @Test
    fun testFirstRunAndSuccess() {
        viewModel.init()
        communication.checkUiState(LoadingCurrenciesUiState.Loading)
        repository.checkLoadCurrenciesCalledCount(1)

        runAsync.pingResult()
        navigation.updateUi(DashboardScreen)
    }

    @Test
    fun testNotFirstRun(){
        repository.setCacheCurrencies()

        viewModel.init()
        repository.checkLoadCurrenciesCalledCount(0)
        navigation.updateUi(DashboardScreen)
    }

    @Test
    fun testFirstRunAndError(){
        repository.loadSuccess = false

        viewModel.init()
        communication.checkUiState(LoadingCurrenciesUiState.Loading)

        runAsync.pingResult()
        communication.checkUiState(LoadingCurrenciesUiState.Error(message = "Service unavailable"))

        viewModel.loadCurrencies()
        communication.checkUiState(LoadingCurrenciesUiState.Loading)

        runAsync.pingResult()
        navigation.updateUi(DashboardScreen)
    }
}

private class FakeRepository: CurrenciesRepository{

    var loadSuccess = true

    private var actualCurrencies = emptyList<String>()

    override fun currencies(): List<String> {
        return  actualCurrencies
    }

    fun setCacheCurrencies() {
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