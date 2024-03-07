package com.example.presentation.subscription

import com.example.domain.settings.PremiumStorage
import com.example.presentation.core.FakeClear
import com.example.presentation.core.FakeNavigation
import com.example.presentation.main.Screen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SubscriptionViewModelTest {

    private lateinit var viewModel: SubscriptionViewModel
    private lateinit var premiumStorage: FakeSavePremiumStorage
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClear

    @Before
    fun setUp() {
        premiumStorage = FakeSavePremiumStorage()
        navigation = FakeNavigation()
        clearViewModel = FakeClear()
        viewModel = SubscriptionViewModel(
            premiumStorage = premiumStorage,
            navigation = navigation,
            clearViewModel = clearViewModel
        )
    }

    @Test
    fun testUserBoughtPremium() {
        viewModel.buyPremium()

        premiumStorage.checkUserIsPremium()
        clearViewModel.clear(SubscriptionViewModel::class.java)
        navigation.checkScreen(Screen.Pop)
    }

    @Test
    fun testUserComeback() {
        viewModel.comeback()

        premiumStorage.checkUserIsNotPremium()
        clearViewModel.clear(SubscriptionViewModel::class.java)
        navigation.checkScreen(Screen.Pop)
    }
}

private class FakeSavePremiumStorage : PremiumStorage.Save {

    private var isUserPremium = false

    override fun savePremium() {
        isUserPremium = true
    }

    fun checkUserIsPremium() {
        assertEquals(true, isUserPremium)
    }

    fun checkUserIsNotPremium() {
        assertEquals(false, isUserPremium)
    }
}