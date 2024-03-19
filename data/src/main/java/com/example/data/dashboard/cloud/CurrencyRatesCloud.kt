package com.example.data.dashboard.cloud

import com.google.gson.annotations.SerializedName

data class CurrencyRatesCloud(
    @SerializedName("rates")
    private val rates: Map<String, Double>
) {

    fun rate(currency: String): Double = rates.getValue(currency)
}