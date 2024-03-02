package com.example.frankfurter

import android.content.Context
import com.example.data.core.CacheModule
import com.example.data.core.ProvideResources
import com.example.data.dashboard.cache.FavoriteCurrenciesCacheDataSource
import com.example.data.loadcurrencies.cache.CurrenciesCacheDataSource
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.RunAsync
import com.example.presentation.main.Navigation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Core {

    fun navigation(): Navigation.Mutable

    fun runAsync(): RunAsync

    fun clearViewModel(): ClearViewModel

    fun provideResources(): ProvideResources

    fun cacheModule(): CacheModule

    fun retrofit(): Retrofit

    fun favoriteCurrenciesCacheDataSource(): FavoriteCurrenciesCacheDataSource.Mutable

    fun currenciesCacheDataSource(): CurrenciesCacheDataSource.Mutable

    class Base(context: Context, private val clearViewModel: ClearViewModel) : Core {

        private val navigation: Navigation.Mutable = Navigation.Base()
        private val runAsync = RunAsync.Base()
        private val provideResources: ProvideResources = BaseProvideResources(context)
        private val cacheModule: CacheModule = CacheModule.Base(context)
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }).build()
            )
            .build()

        override fun navigation() = navigation

        override fun runAsync() = runAsync

        override fun clearViewModel() = clearViewModel

        override fun provideResources() = provideResources

        override fun cacheModule() = cacheModule

        override fun retrofit() = retrofit

        override fun favoriteCurrenciesCacheDataSource() = FavoriteCurrenciesCacheDataSource.Base(
            cacheModule().database().favoriteCurrenciesDao()
        )

        override fun currenciesCacheDataSource() = CurrenciesCacheDataSource.Base(
            cacheModule().database().currenciesDao()
        )
    }
}