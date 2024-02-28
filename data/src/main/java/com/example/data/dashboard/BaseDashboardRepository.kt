package com.example.data.dashboard

import com.example.data.core.ProvideResources
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import java.net.UnknownHostException

class BaseDashboardRepository(
    private val favoriteCacheDataSource: FavoriteCurrenciesCacheDataSource.Mutable,
    private val provideResources: ProvideResources,
    private val mapper: DashboardItemMapper
) : DashboardRepository {

    override suspend fun dashboards(): DashboardResult {

        val favoriteCurrencies = favoriteCacheDataSource.favoriteCurrencies()

        return if (favoriteCurrencies.isEmpty()) {
            DashboardResult.Empty
        } else {
            try {
                val listOfItems: List<DashboardItem> = mapper.map(favoriteCurrencies)
                DashboardResult.Success(listOfItems = listOfItems)
            } catch (e: Exception) {
                val message = if (e is UnknownHostException) {
                    provideResources.noInternetConnectionMessage()
                } else {
                    provideResources.serviceUnavailableMessage()
                }
                DashboardResult.Error(message = message)
            }
        }
    }
}

