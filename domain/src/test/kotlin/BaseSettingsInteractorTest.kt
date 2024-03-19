import com.example.domain.settings.PremiumStorage
import com.example.domain.settings.SaveResult
import com.example.domain.settings.SettingsInteractor
import com.example.domain.settings.SettingsRepository
import kotlinx.coroutines.runBlocking

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class BaseSettingsInteractorTest {

    private lateinit var interactor: SettingsInteractor
    private lateinit var repository: FakeSettingsRepository
    private lateinit var premiumStorage: FakeReadPremiumStorage

    @Before
    fun setUp() {
        repository = FakeSettingsRepository()
        premiumStorage = FakeReadPremiumStorage()
        interactor = SettingsInteractor.Base(
            repository = repository,
            premiumStorage = premiumStorage,
            maxFreeSavedPairsCount = 2
        )
    }

    @Test
    fun mainScenario(): Unit = runBlocking {
        assertEquals(listOf("USD", "EUR", "JPY"), interactor.allCurrencies())

        var availableCurrencies = interactor.availableDestinations("USD")
        assertEquals(listOf("EUR", "JPY"), availableCurrencies)

        var saveResult = interactor.save("USD", "EUR")
        assertEquals(SaveResult.Success, saveResult)
        repository.checkSavedCurrencyPairs(Pair("USD", "EUR"))

        availableCurrencies = interactor.availableDestinations("USD")
        assertEquals(listOf("JPY"), availableCurrencies)

        saveResult = interactor.save("USD", "JPY")
        assertEquals(SaveResult.Success, saveResult)
        repository.checkSavedCurrencyPairs(Pair("USD", "EUR"), Pair("USD", "JPY"))

        availableCurrencies = interactor.availableDestinations("EUR")
        assertEquals(listOf("USD", "JPY"), availableCurrencies)

        saveResult = interactor.save("EUR", "JPY")
        assertEquals(SaveResult.RequirePremium, saveResult)
        repository.checkSavedCurrencyPairs(Pair("USD", "EUR"), Pair("USD", "JPY"))

        premiumStorage.userIsPremium()
        saveResult = interactor.save("EUR", "JPY")
        assertEquals(SaveResult.Success, saveResult)
        repository.checkSavedCurrencyPairs(
            Pair("USD", "EUR"),
            Pair("USD", "JPY"),
            Pair("EUR", "JPY")
        )
    }
}

private class FakeSettingsRepository : SettingsRepository {

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

    override suspend fun save(from: String, to: String) {
        savedPairs.add(Pair(from, to))
    }

    override suspend fun savedPairsCount() = savedPairs.count()

    fun checkSavedCurrencyPairs(vararg pairs: Pair<String, String>) {
        assertEquals(pairs.toList(), savedPairs)
    }
}

private class FakeReadPremiumStorage : PremiumStorage.Read {

    private var isPremium = false

    override fun isUserPremium() = isPremium

    fun userIsPremium() {
        isPremium = true
    }
}