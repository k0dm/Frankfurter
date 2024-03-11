package com.example.presentation.settings

import com.example.domain.settings.SaveResult
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.main.Navigation
import com.example.presentation.subscription.SubscriptionScreen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseSaveResultMapper @Inject constructor(
    private val navigation: Navigation.Update,
) : SaveResult.Mapper {

    override fun mapSavedSuccessfully() = navigation.updateUi(DashboardScreen)

    override fun mapRequireSubscription() = navigation.updateUi(SubscriptionScreen)
}