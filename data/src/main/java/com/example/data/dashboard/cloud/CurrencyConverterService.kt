package com.example.data.dashboard.cloud

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterService {

    @GET("latest")
    fun exchangeRate(@Query("from") from: String, @Query("to") to: String): Call<CurrencyRatesCloud>
}