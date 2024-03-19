package com.example.frankfurter.modules

import com.example.domain.settings.PremiumStorage
import com.example.frankfurter.BasePremiumStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SubscriptionModule {

    @Binds
    abstract fun bindSavePremiumStorage(premiumStorage: BasePremiumStorage): PremiumStorage.Save
}