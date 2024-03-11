package com.example.frankfurter.modules

import com.example.data.dashboard.cloud.CurrencyConverterService
import com.example.data.loadcurrencies.cloud.CurrenciesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BASIC)
                    }).build()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyConverterService(retrofit: Retrofit): CurrencyConverterService =
        retrofit.create(CurrencyConverterService::class.java)

    @Provides
    @Singleton
    fun provideCurrenciesService(retrofit: Retrofit): CurrenciesService =
        retrofit.create(CurrenciesService::class.java)
}