package com.example.frankfurter.modules

import android.content.Context
import androidx.room.Room
import com.example.data.core.CurrenciesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CurrenciesDatabase {
        return Room.databaseBuilder(
            context,
            CurrenciesDatabase::class.java,
            "currencies_db"
        ).build()
    }

    @Provides
    fun provideCurrenciesDao(database: CurrenciesDatabase) = database.currenciesDao()

    @Provides
    fun provideFavoriteCurrenciesDao(database: CurrenciesDatabase) =
        database.favoriteCurrenciesDao()
}