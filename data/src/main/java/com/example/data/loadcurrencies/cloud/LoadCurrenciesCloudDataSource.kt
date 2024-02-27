package com.example.data.loadcurrencies.cloud

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface LoadCurrenciesCloudDataSource {

    fun currencies(): List<String>

    class Base(private val currenciesService: CurrenciesService) : LoadCurrenciesCloudDataSource {

        constructor() : this(
            Retrofit.Builder()
                .baseUrl("https://api.frankfurter.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }).build()
                )
                .build()
                .create(CurrenciesService::class.java)
        )

        override fun currencies() = currenciesService.currencies().execute().body()!!.keys.toList()
    }
}