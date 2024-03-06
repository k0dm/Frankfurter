package com.example.presentation.settings

import com.example.domain.settings.SaveResult
import com.example.domain.settings.SettingsInteractor
import com.example.presentation.core.FakeClear
import com.example.presentation.core.FakeNavigation
import com.example.presentation.core.FakeRunAsync
import com.example.presentation.core.FakeUpdateNavigation
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.subscription.SubscriptionScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var navigation: FakeUpdateNavigation
    private lateinit var communication: FakeSettingsCommunication
    private lateinit var interactor: FakeSettingsInteractor
    private lateinit var runAsync: FakeRunAsync
    private lateinit var clearViewModel: FakeClear
    private lateinit var bundleWrapper: FakeSettingsBundleWrapper

    @Before
    fun setUp() {
        navigation = FakeNavigation()
        communication = FakeSettingsCommunication()
        interactor = FakeSettingsInteractor(maxFreeSavedPairsCount = 2)
        runAsync = FakeRunAsync()
        clearViewModel = FakeClear()
        bundleWrapper = FakeSettingsBundleWrapper()
        viewModel = SettingsViewModel(
            navigation = navigation,
            communication = communication,
            interactor = interactor,
            runAsync = runAsync,
            clearViewModel = clearViewModel
        )
    }

    @Test
    fun mainScenario() {
        viewModel.init(bundleWrapper)
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.Initial(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseFrom(currency = "USD")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD", chosen = true),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseTo(from = "USD", to = "EUR")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.ReadyToSave(
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "EUR", chosen = true),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.save(from = "USD", to = "EUR")
        runAsync.pingResult()
        interactor.checkSavedCurrencyPairs(Pair("USD", "EUR"))
        clearViewModel.checkClearCalled(listOf(SettingsViewModel::class.java))
        navigation.checkScreen(DashboardScreen)
    }

    @Test
    fun testUserAlreadySavedCurrencyPairAndCheckPairAfterComeback() {
        mainScenario()
        viewModel.init(bundleWrapper)
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.Initial(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseFrom(currency = "USD")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD", chosen = true),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseFrom(currency = "EUR")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "EUR", chosen = true),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseTo(from = "EUR", to = "JPY")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.ReadyToSave(
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "JPY", chosen = true),
                )
            )
        )

        viewModel.chooseFrom(currency = "USD")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD", chosen = true),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseTo(from = "USD", to = "JPY")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.ReadyToSave(
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "JPY", chosen = true),
                )
            )
        )

        viewModel.save(from = "USD", to = "JPY")
        runAsync.pingResult()
        clearViewModel.checkClearCalled(
            listOf(SettingsViewModel::class.java, SettingsViewModel::class.java)
        )
        navigation.checkScreen(DashboardScreen)
        interactor.checkSavedCurrencyPairs(Pair("USD", "EUR"), Pair("USD", "JPY"))
    }

    @Test
    fun testEmptyAvailableDestinationCurrencies() {
        testUserAlreadySavedCurrencyPairAndCheckPairAfterComeback()
        viewModel.init(bundleWrapper)
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.Initial(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseFrom("USD")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD", chosen = true),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(CurrencyUi.Empty)
            )
        )
    }

    @Test
    fun testRecreateActivity() {
        bundleWrapper.recreate()

        viewModel.init(bundleWrapper)
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.Initial(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        bundleWrapper.recreateWithFrom()
        viewModel.init(bundleWrapper)
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD", chosen = true),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        bundleWrapper.recreateWithFromAndTo()
        viewModel.init(bundleWrapper)
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD", chosen = true),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.ReadyToSave(
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "EUR", chosen = true),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )
    }

    @Test
    fun testUserIsNotPremium() {
        testUserAlreadySavedCurrencyPairAndCheckPairAfterComeback()

        viewModel.init(bundleWrapper)
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.Initial(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "EUR"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseFrom(currency = "EUR")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.AvailableDestinations(
                fromCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "EUR", chosen = true),
                    CurrencyUi.Base(value = "JPY"),
                ),
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "JPY"),
                )
            )
        )

        viewModel.chooseTo(from = "EUR", to = "JPY")
        runAsync.pingResult()
        communication.checkUiState(
            SettingsUiState.ReadyToSave(
                toCurrencies = listOf(
                    CurrencyUi.Base(value = "USD"),
                    CurrencyUi.Base(value = "JPY", chosen = true)
                )
            )
        )

        viewModel.save(from = "EUR", to = "JPY")
        runAsync.pingResult()
        interactor.checkSavedCurrencyPairs(Pair("USD", "EUR"), Pair("USD", "JPY"))
        clearViewModel.checkClearCalled(
            listOf(
                SettingsViewModel::class.java,
                SettingsViewModel::class.java,
            )
        )
        navigation.checkScreen(SubscriptionScreen)

        interactor.userBoughtPremium()

        viewModel.save(from = "EUR", to = "JPY")
        runAsync.pingResult()
        interactor.checkSavedCurrencyPairs(
            Pair("USD", "EUR"),
            Pair("USD", "JPY"),
            Pair("EUR", "JPY")
        )
        clearViewModel.checkClearCalled(
            listOf(
                SettingsViewModel::class.java,
                SettingsViewModel::class.java,
                SettingsViewModel::class.java
            )
        )
        navigation.checkScreen(DashboardScreen)
    }
}

private class FakeSettingsInteractor(private val maxFreeSavedPairsCount: Int) : SettingsInteractor {

    private val allCurrencies = listOf("USD", "EUR", "JPY")

    override suspend fun allCurrencies() = allCurrencies

    override suspend fun availableDestinations(from: String): List<String> {
        return mutableListOf<String>().apply {
            addAll(allCurrencies)
            remove(from)
            removeAll(
                savedPairs
                    .filter { pair -> pair.first == from }
                    .map { pair -> pair.second }
            )
        }
    }

    private val savedPairs = mutableListOf<Pair<String, String>>()
    private var isUserPremium = false

    override suspend fun save(from: String, to: String): SaveResult {
        return if (savedPairs.size < maxFreeSavedPairsCount || isUserPremium) {
            savedPairs.add(Pair(from, to))
            SaveResult.Success
        } else {
            SaveResult.RequirePremium
        }
    }

    fun checkSavedCurrencyPairs(vararg pairs: Pair<String, String>) {
        assertEquals(pairs.toList(), savedPairs)
    }

    fun userBoughtPremium() {
        isUserPremium = true
    }
}

private class FakeSettingsBundleWrapper() : SettingsBundleWrapper {

    private var isEmpty = true

    override fun isEmpty() = isEmpty

    fun recreate() {
        isEmpty = false
    }

    override fun save(from: String, to: String) = Unit

    private var from = ""
    private var to = ""

    fun recreateWithFrom() {
        from = "USD"
    }

    fun recreateWithFromAndTo() {
        from = "USD"
        to = "EUR"
    }

    override fun restore() = Pair(from, to)
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