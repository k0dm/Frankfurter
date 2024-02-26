package com.example.frankfurter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.data.loadcurrencies.BaseLoadCurrenciesRepository
import com.example.data.loadcurrencies.cloud.CurrenciesService
import com.example.data.loadcurrencies.cloud.LoadCurrenciesCloudDataSource
import com.example.data.loadcurrencies.data.CurrenciesCacheDataSource
import com.example.data.loadcurrencies.data.CurrenciesDatabase
import com.example.presentation.core.ProvideViewModel
import com.example.presentation.core.RunAsync
import com.example.presentation.loadingcurrencies.LoadingCurrenciesCommunication
import com.example.presentation.loadingcurrencies.LoadingCurrenciesViewModel
import com.example.presentation.main.MainViewModel
import com.example.presentation.main.Navigation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
class BaseProvideViewModel(context: Context) : ProvideViewModel {

    private val navigation = Navigation.Base()
    private val runAsync = RunAsync.Base()

    private val currenciesService = Retrofit.Builder()
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

    private val currenciesDao = Room.databaseBuilder(
        context,
        CurrenciesDatabase::class.java,
        "currencies_db"
    ).build().currenciesDao()

    private val provideResources = BaseProvideResources(context)

    private val repository = BaseLoadCurrenciesRepository(
        cloudDataSource = LoadCurrenciesCloudDataSource.Base(currenciesService),
        cacheDataSource = CurrenciesCacheDataSource.Base(currenciesDao),
        provideResources = provideResources
    )

    override fun <T : ViewModel> viewModel(clazz: Class<out T>) = when (clazz) {
        MainViewModel::class.java -> MainViewModel(
            navigation = navigation
        )

        LoadingCurrenciesViewModel::class.java -> LoadingCurrenciesViewModel(
            navigation = navigation,
            communication = LoadingCurrenciesCommunication.Base(),
            repository = repository,
            runAsync = runAsync
        )


        else -> throw IllegalStateException("No such viewModel with class: $clazz")
    } as T
}
