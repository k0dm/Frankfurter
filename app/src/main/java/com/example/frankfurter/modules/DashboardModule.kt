package com.example.frankfurter.modules

import com.example.data.dashboard.DashboardItemsDatasource
import com.example.data.dashboard.HandleError
import com.example.data.dashboard.cache.CurrentDate
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.dashboard.cloud.CurrencyConverterCloudDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.domain.dashboard.ForegroundDownloadWorkManagerWrapper
import com.example.frankfurter.ProvideInstance
import com.example.presentation.dashboard.BaseDashboardItemMapper
import com.example.presentation.dashboard.BaseDashboardResultMapper
import com.example.presentation.dashboard.BaseForegroundDownloadWorkManagerWrapper
import com.example.presentation.dashboard.CurrencyPairDelimiter
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
    @Singleton
    abstract fun bindCloudDataSource(cloudDataSource: LoadCurrenciesCloudDataSource.Base): LoadCurrenciesCloudDataSource

    @Binds
    @Singleton
    abstract fun bindMutableCacheDataSource(cacheDataSource: CurrenciesCacheDataSource.Base): CurrenciesCacheDataSource.Mutable

    @Binds
    @Singleton
    abstract fun bindReadAndDeleteCacheDataSource(cacheDataSource: FavoriteCurrenciesCacheDataSource.Base): FavoriteCurrenciesCacheDataSource.ReadAndDelete

    @Binds
    @Singleton
    abstract fun bindSaveCacheDataSource(cacheDataSource: FavoriteCurrenciesCacheDataSource.Base): FavoriteCurrenciesCacheDataSource.Save

    @Binds
    @Singleton
    abstract fun bindDataSource(datasource: DashboardItemsDatasource.Base): DashboardItemsDatasource

    @Binds
    @Singleton
    abstract fun bindConverterCloudDataSource(cloudDataSource: CurrencyConverterCloudDataSource.Base): CurrencyConverterCloudDataSource

    @Binds
    @Singleton
    abstract fun bindAddDelimiter(currencyPairDelimiter: CurrencyPairDelimiter.Base): CurrencyPairDelimiter.AddDelimiter

    @Binds
    @Singleton
    abstract fun bindDashboardResultMapper(mapper: BaseDashboardResultMapper): DashboardResult.Mapper

    @Binds
    @Singleton
    abstract fun bindDashboardItemMapper(mapper: BaseDashboardItemMapper): DashboardItem.Mapper<DashboardCurrencyPairUi>

    @Binds
    @Singleton
    abstract fun bindMutableCurrencyPairDelimiter(currencyPairDelimiter: CurrencyPairDelimiter.Base): CurrencyPairDelimiter.Mutable

    @Binds
    @Singleton
    abstract fun bindForegroundWrapper(foregroundWrapper: BaseForegroundDownloadWorkManagerWrapper): ForegroundDownloadWorkManagerWrapper

    companion object {
        @Provides
        @Singleton
        fun provideRepository(
            provideInstance: ProvideInstance,
            cloudDataSource: LoadCurrenciesCloudDataSource,
            cacheDataSource: CurrenciesCacheDataSource.Mutable,
            favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
            dashboardItemsDatasource: DashboardItemsDatasource.Base,
            handleError: HandleError,
            foregroundWrapper: ForegroundDownloadWorkManagerWrapper
        ): DashboardRepository = provideInstance.provideDashboardRepository(
            cloudDataSource,
            cacheDataSource,
            favoriteCacheDataSource,
            dashboardItemsDatasource,
            handleError,
            foregroundWrapper
        )

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
    }
}