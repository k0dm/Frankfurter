package com.example.data.dashboard

import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import kotlinx.coroutines.CoroutineScope

class BaseDashboardRepository(
    private val favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.ReadAndDelete,
    private val dashboardItemsDatasource: DashboardItemsDatasource,
    private val handleError: HandleError
) : DashboardRepository {

    override suspend fun dashboards(viewModelScope: CoroutineScope): DashboardResult {

        val favoriteCurrencies = favoriteCacheDataSource.favoriteCurrencies()

        return if (favoriteCurrencies.isEmpty()) {
            DashboardResult.Empty
        } else {
            try {
                val listOfItems: List<DashboardItem> =
                    dashboardItemsDatasource.dashboardItems(favoriteCurrencies, viewModelScope)
                DashboardResult.Success(listOfItems = listOfItems)
            } catch (e: Exception) {
                DashboardResult.Error(message = handleError.handle(e))
            }
        }
    }

    override suspend fun removePair(
        from: String,
        to: String,
        viewModelScope: CoroutineScope
    ): DashboardResult {
        favoriteCacheDataSource.delete(
            favoriteCacheDataSource.favoriteCurrencies()
                .find { it.fromCurrency == from && it.toCurrency == to }!!

        )
        return dashboards(viewModelScope)
    }
}