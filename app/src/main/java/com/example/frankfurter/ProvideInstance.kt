package com.example.frankfurter

import com.example.data.dashboard.BaseDashboardRepository
import com.example.data.dashboard.DashboardItemsDatasource
import com.example.data.dashboard.HandleError
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.data.settings.BaseSettingsRepository
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.domain.settings.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

interface ProvideInstance {

    fun provideDashboardRepository(
        cloudDataSource: LoadCurrenciesCloudDataSource,
        cacheDataSource: CurrenciesCacheDataSource.Mutable,
        favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
        dashboardItemsDatasource: DashboardItemsDatasource.Base,
        handleError: HandleError
    ): DashboardRepository

    fun provideSettingsRepository(
        currenciesCacheDataSource: CurrenciesCacheDataSource.Base,
        favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.Base
    ): SettingsRepository

    fun provideMaxFreeSavedPairsCount(): Int

    @Singleton
    class Base @Inject constructor() : ProvideInstance {

        override fun provideDashboardRepository(
            cloudDataSource: LoadCurrenciesCloudDataSource,
            cacheDataSource: CurrenciesCacheDataSource.Mutable,
            favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
            dashboardItemsDatasource: DashboardItemsDatasource.Base,
            handleError: HandleError
        ) = BaseDashboardRepository(
            cloudDataSource,
            cacheDataSource,
            favoriteCacheDataSource,
            dashboardItemsDatasource,
            handleError
        )

        override fun provideSettingsRepository(
            currenciesCacheDataSource: CurrenciesCacheDataSource.Base,
            favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.Base
        ) = BaseSettingsRepository(currenciesCacheDataSource, favoriteCurrenciesCacheDataSource)

        override fun provideMaxFreeSavedPairsCount() = 5
    }

    @Singleton
    class Mock @Inject constructor() : ProvideInstance {

        private class MockDashboardRepository : DashboardRepository {

            override suspend fun dashboards(): DashboardResult {
                return if (loadFailed) {
                    loadFailed = false
                    DashboardResult.Error("No internet connection")
                } else if (favoriteCurrencies.isEmpty())
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
            ): DashboardResult {
                favoriteCurrencies.remove(Pair(from, to))
                return dashboards()
            }
        }

        private companion object {
            var loadFailed = true
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

        override fun provideDashboardRepository(
            cloudDataSource: LoadCurrenciesCloudDataSource,
            cacheDataSource: CurrenciesCacheDataSource.Mutable,
            favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Base,
            dashboardItemsDatasource: DashboardItemsDatasource.Base,
            handleError: HandleError
        ): DashboardRepository = MockDashboardRepository()

        override fun provideSettingsRepository(
            currenciesCacheDataSource: CurrenciesCacheDataSource.Base,
            favoriteCurrenciesCacheDataSource: FavoriteCurrenciesCacheDataSource.Base
        ): SettingsRepository = MockSettingsRepository()

        override fun provideMaxFreeSavedPairsCount() = 2
    }
}