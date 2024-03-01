package com.example.presentation.settings

import com.example.domain.settings.SettingsRepository
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.FakeClear
import com.example.presentation.core.FakeNavigation
import com.example.presentation.core.FakeRunAsync
import com.example.presentation.core.FakeUpdateNavigation
import com.example.presentation.dashboard.DashboardScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var navigation: FakeUpdateNavigation
    private lateinit var communication: FakeSettingsCommunication
    private lateinit var repository: FakeSettingsRepository
    private lateinit var runAsync: FakeRunAsync
    private lateinit var clearViewModel: ClearViewModel

    @Before
    fun setUp() {
        navigation = FakeNavigation()
        communication = FakeSettingsCommunication()
        repository = FakeSettingsRepository()
        runAsync = FakeRunAsync()
        clearViewModel = FakeClear()
        viewModel = SettingsViewModel(
            navigation = navigation,
            communication = communication,
            repository = repository,
            runAsync = runAsync,
            clearViewModel = clearViewModel
        )
    }

    @Test
    fun mainScenario() {
        viewModel.init()
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.Initial(
                fromCurrencies = listOf(
                    CurrencyUi(value = "USD"),
                    CurrencyUi(value = "EUR"),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                )
            )
        )

        viewModel.chooseFrom(currency = "USD")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi(value = "USD", chosen = true),
                    CurrencyUi(value = "EUR"),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                ),
                toCurrencies = listOf(
                    CurrencyUi(value = "EUR"),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                )
            )
        )

        viewModel.chooseTo(currency = "EUR")
        communication.checkUiState(
            SettingsUiState.ReadyToSave(
                toCurrencies = listOf(
                    CurrencyUi(value = "EUR", chosen = true),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                )
            )
        )

        viewModel.save()
        runAsync.pingResult()
        repository.checkSavedCurrencyPairs(Pair("USD", "EUR"))
        clearViewModel.clear(SettingsViewModel::class.java)
        navigation.checkScreen(DashboardScreen)
    }

    @Test
    fun testUserAlreadySavedCurrencyPairAndCheckPairAfterComeback() {
        mainScenario()
        viewModel.init()
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.Initial(
                fromCurrencies = listOf(
                    CurrencyUi(value = "USD"),
                    CurrencyUi(value = "EUR"),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                )
            )
        )

        viewModel.chooseFrom(currency = "USD")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi(value = "USD", chosen = true),
                    CurrencyUi(value = "EUR"),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                ),
                toCurrencies = listOf(
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                )
            )
        )

        viewModel.chooseFrom(currency = "EUR")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi(value = "USD"),
                    CurrencyUi(value = "EUR", chosen = true),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                ),
                toCurrencies = listOf(
                    CurrencyUi(value = "USD"),
                    CurrencyUi(value = "JPY"),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                )
            )
        )

        viewModel.chooseTo(currency = "JPY")
        communication.checkUiState(
            SettingsUiState.ReadyToSave(
                toCurrencies = listOf(
                    CurrencyUi(value = "USD"),
                    CurrencyUi(value = "JPY", chosen = true),
                    CurrencyUi(value = "AUD"),
                    CurrencyUi(value = "BRL")
                )
            )
        )

        viewModel.goToDashboard()
        clearViewModel.clear(SettingsViewModel::class.java)
        navigation.checkScreen(DashboardScreen)

        repository.checkSavedCurrencyPairs(Pair("USD", "EUR"))
    }
}

private class FakeSettingsRepository : SettingsRepository {

    private val allCurrencies = listOf("USD", "EUR", "JPY", "AUD", "BRL")

    override suspend fun allCurrencies() = allCurrencies

    override suspend fun availableDestinations(from: String): List<String> {
        return mutableListOf<String>().also {
            it.addAll(allCurrencies)
            it.remove(from)
            it.removeAll(
                savedPairs
                    .filter { pair -> pair.first == from }
                    .map { pair -> pair.second }
            )
        }
    }

    private val savedPairs = mutableListOf<Pair<String, String>>()

    override suspend fun save(from: String, to: String) {
        savedPairs.add(Pair(from, to))
    }

    fun checkSavedCurrencyPairs(vararg pairs: Pair<String, String>) {
        assertEquals(pairs.toList(), savedPairs)
    }
}

private class FakeSettingsCommunication : SettingsCommunication {

    private var actualUiState: SettingsUiState = SettingsUiState.Empty

    override fun updateUi(value: SettingsUiState) {
        actualUiState = value
    }

    fun checkUiState(expectedUiState: SettingsUiState) {
        assertEquals(expectedUiState, actualUiState)
    }
}