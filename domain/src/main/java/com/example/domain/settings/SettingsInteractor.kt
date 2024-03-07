package com.example.domain.settings

interface SettingsInteractor {

    suspend fun allCurrencies(): List<String>

    suspend fun availableDestinations(from: String): List<String>

    suspend fun save(from: String, to: String): SaveResult

    class Base(
        private val repository: SettingsRepository,
        private val premiumStorage: PremiumStorage.Read,
        private val maxFreeSavedPairsCount: Int
    ) : SettingsInteractor {

        override suspend fun allCurrencies(): List<String> = repository.allCurrencies()

        override suspend fun availableDestinations(from: String) =
            repository.availableDestinations(from)

        override suspend fun save(from: String, to: String): SaveResult {
            return if (premiumStorage.isUserPremium() || repository.savedPairsCount() < maxFreeSavedPairsCount) {
                repository.save(from, to)
                SaveResult.Success
            } else {
                SaveResult.RequirePremium
            }
        }
    }
}