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
import com.example.frankfurter.ProvideInstance
import com.example.presentation.dashboard.BaseDashboardItemMapper
import com.example.presentation.dashboard.BaseDashboardResultMapper
import com.example.presentation.dashboard.CurrencyPairDelimiter
import com.example.presentation.dashboard.RatesFormatter
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@SuppressWarnings("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class DashboardModule {

    @Binds
    @ViewModelScoped
    abstract fun bindReadAndDeleteCacheDataSource(cacheDataSource: FavoriteCurrenciesCacheDataSource.Base): FavoriteCurrenciesCacheDataSource.ReadAndDelete

    @Binds
    @ViewModelScoped
    abstract fun bindSaveCacheDataSource(cacheDataSource: FavoriteCurrenciesCacheDataSource.Base): FavoriteCurrenciesCacheDataSource.Save

    @Binds
    @ViewModelScoped
    abstract fun bindCloudDataSource(cloudDataSource: LoadCurrenciesCloudDataSource.Base): LoadCurrenciesCloudDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindMutableCacheDataSource(cacheDataSource: CurrenciesCacheDataSource.Base): CurrenciesCacheDataSource.Mutable

    @Binds
    @ViewModelScoped
    abstract fun bindDataSource(datasource: DashboardItemsDatasource.Base): DashboardItemsDatasource

    @Binds
    @ViewModelScoped
    abstract fun bindConverterCloudDataSource(cloudDataSource: CurrencyConverterCloudDataSource.Base): CurrencyConverterCloudDataSource

    @Binds
    @ViewModelScoped
    abstract fun bindAddDelimiter(currencyPairDelimiter: CurrencyPairDelimiter.Base): CurrencyPairDelimiter.AddDelimiter

    @Binds
    @ViewModelScoped
    abstract fun bindMutableCurrencyPairDelimiter(currencyPairDelimiter: CurrencyPairDelimiter.Base): CurrencyPairDelimiter.Mutable

    @Binds
    @ViewModelScoped
    abstract fun bindDashboardResultMapper(mapper: BaseDashboardResultMapper): DashboardResult.Mapper

    @Binds
    @ViewModelScoped
    abstract fun bindDashboardItemMapper(mapper: BaseDashboardItemMapper): DashboardItem.Mapper<DashboardCurrencyPairUi>

    companion object {

        @Provides
        @ViewModelScoped
        fun provideCurrentDate(): CurrentDate = CurrentDate.Base()

        @Provides
        @ViewModelScoped
        fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @ViewModelScoped
        fun provideCurrencyPairDelimiter(): CurrencyPairDelimiter.Base =
            CurrencyPairDelimiter.Base()

        @Provides
        @ViewModelScoped
        fun provideRatesFormatter(): RatesFormatter = RatesFormatter.Base()

        @Provides
        @ViewModelScoped
        fun provideRepository(
            provideInstance: ProvideInstance,
            cloudDataSource: LoadCurrenciesCloudDataSource,
            cacheDataSource: CurrenciesCacheDataSource.Mutable,
            favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
            dashboardItemsDatasource: DashboardItemsDatasource.Base,
            handleError: HandleError
        ): DashboardRepository = provideInstance.provideDashboardRepository(
            cloudDataSource,
            cacheDataSource,
            favoriteCacheDataSource,
            dashboardItemsDatasource,
            handleError
        )
    }
}