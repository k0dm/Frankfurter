package com.example.frankfurter.modules

import com.example.frankfurter.Core
import com.example.presentation.subscription.SubscriptionViewModel

class SubscriptionModule(private val core: Core) : Module<SubscriptionViewModel> {

    override fun viewModel() = SubscriptionViewModel(
        premiumStorage = core.premiumStorage(),
        navigation = core.navigation(),
        clearViewModel = core.clearViewModel()
    )
}