package com.example.presentation.settings

import com.example.domain.settings.SaveResult
import com.example.presentation.core.ClearViewModel
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation
import com.example.presentation.subscription.SubscriptionScreen

class BaseSaveMapperBase(
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : SaveResult.Mapper {

    override fun mapSavedSuccessfully() {
        clearViewModel.clear(SettingsViewModel::class.java)
        navigation.updateUi(DashboardScreen)
    }

    override fun mapRequireSubscription() {
        navigation.updateUi(SubscriptionScreen)
    }
}