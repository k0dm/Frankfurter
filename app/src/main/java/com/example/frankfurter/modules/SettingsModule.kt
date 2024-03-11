package com.example.frankfurter.modules

import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.domain.settings.SaveResult
import com.example.domain.settings.SettingsInteractor
import com.example.domain.settings.SettingsRepository
import com.example.frankfurter.BasePremiumStorage
import com.example.frankfurter.ProvideInstance
import com.example.presentation.settings.BaseSaveResultMapper
import com.example.presentation.settings.SettingsCommunication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindCommunication(communication: SettingsCommunication.Base): SettingsCommunication

    @Binds
    abstract fun bindCurrenciesCacheDataSource(cacheDataSource: CurrenciesCacheDataSource.Base): CurrenciesCacheDataSource.Read

    @Binds
    abstract fun bindFavoriteCurrenciesCacheDataSource(cacheDataSource: FavoriteCurrenciesCacheDataSource.Base): FavoriteCurrenciesCacheDataSource.ReadAndSave

    @Binds
    abstract fun bindInteractor(interactor: SettingsInteractor.Base): SettingsInteractor

    @Binds
    abstract fun bindMapper(mapper: BaseSaveResultMapper): SaveResult.Mapper

    companion object {

        @Provides
        @Singleton
        fun provideRepository(
            provideInstance: ProvideInstance,
            currenciesCacheDataSource: CurrenciesCacheDataSource.Base,
            favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.Base
        ): SettingsRepository = provideInstance.provideSettingsRepository(
            currenciesCacheDataSource,
            favoriteCurrenciesCacheDataSource
        )

        @Provides
        @Singleton
        fun provideInteractor(
            provideInstance: ProvideInstance,
            repository: SettingsRepository,
            premiumStorage: BasePremiumStorage
        ): SettingsInteractor.Base = SettingsInteractor.Base(
            repository = repository,
            premiumStorage = premiumStorage,
            maxFreeSavedPairsCount = provideInstance.provideMaxFreeSavedPairsCount()
        )
    }
}