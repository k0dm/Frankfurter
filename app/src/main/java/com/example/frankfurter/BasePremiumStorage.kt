package com.example.frankfurter

import android.content.Context
import com.example.domain.settings.PremiumStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasePremiumStorage @Inject constructor(
    @ApplicationContext context: Context
) : PremiumStorage.Mutable {

    private val sharedPreferences =
        context.getSharedPreferences("UserPremiumStorage", Context.MODE_PRIVATE)

    override fun isUserPremium() = sharedPreferences.getBoolean(KEY, false)

    override fun savePremium() = sharedPreferences.edit().putBoolean(KEY, true).apply()

    companion object {
        private const val KEY = "PREMIUM_KEY"
    }
}