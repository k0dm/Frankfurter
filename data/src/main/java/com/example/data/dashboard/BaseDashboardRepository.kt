package com.example.data.dashboard

import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.domain.dashboard.ForegroundDownloadWorkManagerWrapper
import javax.inject.Inject

class BaseDashboardRepository @Inject constructor(
    private val cloudDataSource: LoadCurrenciesCloudDataSource,
    private val cacheDataSource: CurrenciesCacheDataSource.Mutable,
    private val favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.ReadAndDelete,
    private val dashboardItemsDatasource: DashboardItemsDatasource,
    private val handleError: HandleError,
    private val foregroundWrapper: ForegroundDownloadWorkManagerWrapper
) : DashboardRepository {

    override suspend fun dashboards(): DashboardResult = try {
        if (cacheDataSource.currencies().isEmpty()) {
            cacheDataSource.saveCurrencies(cloudDataSource.currencies())
        }
        val favoriteCurrencies = favoriteCacheDataSource.favoriteCurrencies()
        if (favoriteCurrencies.isEmpty()) {
            DashboardResult.Empty
        } else {
            if (dashboardItemsDatasource.needToDownloadData(favoriteCurrencies)) {
                foregroundWrapper.start()
                DashboardResult.NoDataYet
            } else {
                val listOfItems: List<DashboardItem> =
                    dashboardItemsDatasource.dashboardItems(favoriteCurrencies)
                DashboardResult.Success(listOfItems = listOfItems)
            }
        }
    } catch (e: Exception) {
        DashboardResult.Error(message = handleError.handle(e))
    }

    override suspend fun downloadDashboards(): DashboardResult = try {
        val favoriteCurrencies = favoriteCacheDataSource.favoriteCurrencies()
        val listOfItems: List<DashboardItem> =
            dashboardItemsDatasource.dashboardItems(favoriteCurrencies)
        DashboardResult.Success(listOfItems = listOfItems)
    } catch (e: Exception) {
        DashboardResult.Error(message = handleError.handle(e))
    }

    override suspend fun removePair(
        from: String,
        to: String,
    ): DashboardResult {
        favoriteCacheDataSource.delete(
            favoriteCacheDataSource.favoriteCurrencies()
                .find { it.fromCurrency == from && it.toCurrency == to }!!
        )
        return dashboards()
    }
}