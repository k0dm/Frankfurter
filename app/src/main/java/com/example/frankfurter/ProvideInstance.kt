package com.example.frankfurter

import com.example.data.core.ProvideResources
import com.example.data.dashboard.BaseDashboardRepository
import com.example.data.dashboard.DashboardItemsDatasource
import com.example.data.dashboard.HandleError
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.BaseLoadCurrenciesRepository
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.data.settings.BaseSettingsRepository
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.domain.loadcurrencies.LoadCurrenciesRepository
import com.example.domain.loadcurrencies.LoadCurrenciesResult
import com.example.domain.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope

interface ProvideInstance {

    fun provideLoadCurrenciesRepository(
        cloudDataSource: LoadCurrenciesCloudDataSource,
        cacheDataSource: CurrenciesCacheDataSource.Mutable,
        provideResources: ProvideResources
    ): LoadCurrenciesRepository

    fun provideDashboardRepository(
        favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
        dashboardItemsDatasource: DashboardItemsDatasource.Base,
        handleError: HandleError.Base
    ): DashboardRepository

    fun provideSettingsRepository(
        currenciesCacheDataSource: CurrenciesCacheDataSource.Base,
        favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.Base
    ): SettingsRepository

    class Base : ProvideInstance {

        override fun provideLoadCurrenciesRepository(
            cloudDataSource: LoadCurrenciesCloudDataSource,
            cacheDataSource: CurrenciesCacheDataSource.Mutable,
            provideResources: ProvideResources
        ) = BaseLoadCurrenciesRepository(cloudDataSource, cacheDataSource, provideResources)

        override fun provideDashboardRepository(
            favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
            dashboardItemsDatasource: DashboardItemsDatasource.Base,
            handleError: HandleError.Base
        ) = BaseDashboardRepository(favoriteCacheDataSource, dashboardItemsDatasource, handleError)

        override fun provideSettingsRepository(
            currenciesCacheDataSource: CurrenciesCacheDataSource.Base,
            favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.Base
        ) = BaseSettingsRepository(currenciesCacheDataSource, favoriteCurrenciesCacheDataSource)
    }

    class Mock : ProvideInstance {

        private class MockLoadCurrenciesRepository : LoadCurrenciesRepository {

            private var counter = 0

            override suspend fun loadCurrencies(): LoadCurrenciesResult {
                return if (counter++ == 0)
                    LoadCurrenciesResult.Error("No internet connection")
                else
                    LoadCurrenciesResult.Success
            }
        }

        private class MockDashboardRepository : DashboardRepository {

            override suspend fun dashboards(viewModelScope: CoroutineScope): DashboardResult {
                return if (favoriteCurrencies.isEmpty())
                    DashboardResult.Empty
                else
                    DashboardResult.Success(listOfItems = favoriteCurrencies.map {
                        DashboardItem.Base(
                            fromCurrency = it.first,
                            toCurrency = it.second,
                            rates = 10.10
                        )
                    })
            }

            override suspend fun removePair(
                from: String,
                to: String,
                viewModelScope: CoroutineScope
            ): DashboardResult {
                favoriteCurrencies.remove(Pair(from, to))
                return dashboards(viewModelScope)
            }
        }

        private companion object {
            val favoriteCurrencies: MutableList<Pair<String, String>> = mutableListOf()
        }

        private class MockSettingsRepository : SettingsRepository {

            override suspend fun allCurrencies(): List<String> = listOf("USD", "EUR", "JPY")

            override suspend fun availableDestinations(from: String): List<String> {
                val allCurrencies = allCurrencies().toMutableList().also { it.remove(from) }

                val matched = favoriteCurrencies
                    .filter { it.first == from }
                    .map { it.second }
                allCurrencies.removeAll(matched)
                return allCurrencies
            }

            override suspend fun save(from: String, to: String) {
                favoriteCurrencies.add(Pair(from, to))
            }

            override suspend fun savedPairsCount() = favoriteCurrencies.size
        }

        override fun provideLoadCurrenciesRepository(
            cloudDataSource: LoadCurrenciesCloudDataSource,
            cacheDataSource: CurrenciesCacheDataSource.Mutable,
            provideResources: ProvideResources
        ): LoadCurrenciesRepository = MockLoadCurrenciesRepository()

        override fun provideDashboardRepository(
            favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
            dashboardItemsDatasource: DashboardItemsDatasource.Base,
            handleError: HandleError.Base
        ): DashboardRepository = MockDashboardRepository()

        override fun provideSettingsRepository(
            currenciesCacheDataSource: CurrenciesCacheDataSource.Base,
            favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.Base
        ): SettingsRepository = MockSettingsRepository()
    }
}