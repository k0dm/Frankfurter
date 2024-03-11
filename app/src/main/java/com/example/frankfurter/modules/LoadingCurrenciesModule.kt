package com.example.frankfurter.modules

import com.example.data.dashboard.HandleError
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.domain.loadcurrencies.LoadCurrenciesRepository
import com.example.domain.loadcurrencies.LoadCurrenciesResult
import com.example.frankfurter.ProvideInstance
import com.example.presentation.loadingcurrencies.BaseLoadCurrenciesResultMapper
import com.example.presentation.loadingcurrencies.LoadingCurrenciesCommunication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoadingCurrenciesModule {

    @Binds
    abstract fun bindCommunication(communication: LoadingCurrenciesCommunication.Base): LoadingCurrenciesCommunication

    @Binds
    abstract fun bindCloudDataSource(cloudDataSource: LoadCurrenciesCloudDataSource.Base): LoadCurrenciesCloudDataSource

    @Binds
    abstract fun bindMutableCacheDataSource(cacheDataSource: CurrenciesCacheDataSource.Base): CurrenciesCacheDataSource.Mutable

    @Binds
    abstract fun bindMapper(mapper: BaseLoadCurrenciesResultMapper): LoadCurrenciesResult.Mapper

    companion object {

        @Provides
        @Singleton
        fun provideRepository(
            provideInstance: ProvideInstance,
            cloudDataSource: LoadCurrenciesCloudDataSource,
            cacheDataSource: CurrenciesCacheDataSource.Mutable,
            handleError: HandleError
        ): LoadCurrenciesRepository = provideInstance.provideLoadCurrenciesRepository(
            cloudDataSource,
            cacheDataSource,
            handleError
        )
    }
}