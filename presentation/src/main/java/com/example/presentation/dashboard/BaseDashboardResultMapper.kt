package com.example.presentation.dashboard

import com.example.domain.dashboard.DashboardItem
import com.example.domain.dashboard.DashboardResult
import com.example.presentation.dashboard.adapter.DashboardCurrencyPairUi
import javax.inject.Inject

class BaseDashboardResultMapper @Inject constructor(
    currencyPairDelimiter: CurrencyPairDelimiter.AddDelimiter,
    private val communication: DashboardCommunication,
    private val mapper: DashboardItem.Mapper<DashboardCurrencyPairUi>
    = BaseDashboardItemMapper(currencyPairDelimiter)
) : DashboardResult.Mapper {

    override fun mapSuccess(listOfItems: List<DashboardItem>) {
        communication.updateUi(DashboardUiState.Success(listOfItems.map { it.map(mapper) }))
    }

    override fun mapError(message: String) {
        communication.updateUi(DashboardUiState.Error(message))
    }

    override fun mapEmpty() {
        communication.updateUi(DashboardUiState.Empty)
    }
}