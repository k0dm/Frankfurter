package com.example.presentation.subscription

import androidx.lifecycle.ViewModel
import com.example.domain.settings.PremiumStorage
import com.example.presentation.main.Navigation
import com.example.presentation.main.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val premiumStorage: PremiumStorage.Save,
    private val navigation: Navigation.Update,
) : ViewModel() {

    fun buyPremium() {
        premiumStorage.savePremium()
        comeback()
    }

    fun comeback() = navigation.updateUi(Screen.Pop)
}