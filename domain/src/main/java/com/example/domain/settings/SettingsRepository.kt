package com.example.domain.settings

interface SettingsRepository {

    suspend fun allCurrencies(): List<String>

    suspend fun getAvailableDestinations(from: String): List<String>

    suspend fun save(from: String, to: String)
}
