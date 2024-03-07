package com.example.frankfurter

import android.content.Context
import com.example.domain.settings.PremiumStorage

class BasePremiumStorage(context: Context) : PremiumStorage.Mutable {

    private val sharedPreferences =
        context.getSharedPreferences("UserPremiumStorage", Context.MODE_PRIVATE)

    override fun isUserPremium() = sharedPreferences.getBoolean(KEY, false)

    override fun savePremium() = sharedPreferences.edit().putBoolean(KEY, true).apply()

    companion object {
        private const val KEY = "PREMIUM_KEY"
    }
}