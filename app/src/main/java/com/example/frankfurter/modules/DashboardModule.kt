package com.example.frankfurter.modules

import com.example.data.dashboard.DashboardItemsDatasource
import com.example.data.dashboard.HandleError
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.frankfurter.ProvideInstance
import com.example.presentation.dashboard.BaseDashboardItemMapper
import com.example.presentation.dashboard.BaseDashboardResultMapper
import com.example.presentation.dashboard.CurrencyPairDelimiter
import com.example.presentation.dashboard.DashboardCommunication
import com.example.presentation.dashboard.RatesFormatter
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {

    @Binds
    abstract fun bindCommunication(communication: DashboardCommunication.Base): DashboardCommunication

    @Binds
    abstract fun bindReadAndDeleteCacheDataSource(cacheDataSource: FavoriteCurrenciesCacheDataSource.Base): FavoriteCurrenciesCacheDataSource.ReadAndDelete

    @Binds
    abstract fun bindSaveCacheDataSource(cacheDataSource: FavoriteCurrenciesCacheDataSource.Base): FavoriteCurrenciesCacheDataSource.Save

    @Binds
    abstract fun bindDataSource(datasource: DashboardItemsDatasource.Base): DashboardItemsDatasource

    @Binds
    abstract fun bindConverterCloudDataSource(cloudDataSource: CurrencyConverterCloudDataSource.Base): CurrencyConverterCloudDataSource

    @Binds
    abstract fun bindAddDelimiter(currencyPairDelimiter: CurrencyPairDelimiter.Base): CurrencyPairDelimiter.AddDelimiter

    @Binds
    abstract fun bindMutableCurrencyPairDelimiter(currencyPairDelimiter: CurrencyPairDelimiter.Base): CurrencyPairDelimiter.Mutable

    @Binds
    abstract fun bindDashboardResultMapper(mapper: BaseDashboardResultMapper): DashboardResult.Mapper

    @Binds
    abstract fun bindDashboardItemMapper(mapper: BaseDashboardItemMapper): DashboardItem.Mapper<DashboardCurrencyPairUi>

    companion object {

        @Provides
        @Singleton
        fun provideCurrentDate(): CurrentDate = CurrentDate.Base()

        @Provides
        @Singleton
        fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Singleton
        fun provideCurrencyPairDelimiter(): CurrencyPairDelimiter.Base =
            CurrencyPairDelimiter.Base()

        @Provides
        @Singleton
        fun provideRatesFormatter(): RatesFormatter = RatesFormatter.Base()

        @Provides
        @Singleton
        fun provideRepository(
            provideInstance: ProvideInstance,
            favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
            dashboardItemsDatasource: DashboardItemsDatasource.Base,
            handleError: HandleError
        ): DashboardRepository = provideInstance.provideDashboardRepository(
            favoriteCacheDataSource,
            dashboardItemsDatasource,
            handleError
        )
    }
}