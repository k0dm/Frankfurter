package com.example.data.dashboard

import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import javax.inject.Inject

class BaseDashboardRepository @Inject constructor(
    private val favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.ReadAndDelete,
    private val dashboardItemsDatasource: DashboardItemsDatasource,
    private val handleError: HandleError
) : DashboardRepository {

    override suspend fun dashboards(): DashboardResult {

        val favoriteCurrencies = favoriteCacheDataSource.favoriteCurrencies()

        return if (favoriteCurrencies.isEmpty()) {
            DashboardResult.Empty
        } else {
            try {
                val listOfItems: List<DashboardItem> =
                    dashboardItemsDatasource.dashboardItems(favoriteCurrencies)
                DashboardResult.Success(listOfItems = listOfItems)
            } catch (e: Exception) {
                DashboardResult.Error(message = handleError.handle(e))
            }
        }
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