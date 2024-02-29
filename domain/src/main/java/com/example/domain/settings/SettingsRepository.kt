package com.example.domain.settings

interface SettingsRepository {

    fun allCurrencies(): List<String>

    fun getAvailableDestinations(from: String): List<String>

    fun save(from: String, to: String)
}
