package com.example.presentation.subscription

import com.example.domain.settings.PremiumStorage
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.CustomViewModel
import com.example.presentation.main.Navigation
import com.example.presentation.main.Screen
import com.example.presentation.settings.SettingsViewModel

class SubscriptionViewModel(
    private val premiumStorage: PremiumStorage.Save,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : CustomViewModel {

    fun buyPremium() {
        premiumStorage.savePremium()
        comeback()
    }

    fun comeback() {
        clearViewModel.clear(SettingsViewModel::class.java)
        navigation.updateUi(Screen.Pop)
    }
}
