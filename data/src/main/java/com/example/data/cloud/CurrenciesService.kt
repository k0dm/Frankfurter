package com.example.data.cloud

import retrofit2.Call
import retrofit2.http.GET

interface CurrenciesService {

    @GET("/currencies")
    fun currencies(): Call<HashMap<String, String>>
}