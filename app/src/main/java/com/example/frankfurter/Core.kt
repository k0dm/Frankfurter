package com.example.frankfurter

import android.content.Context
import com.example.data.core.ProvideResources
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.RunAsync
import com.example.presentation.main.Navigation

interface Core {

    fun navigation(): Navigation.Mutable

    fun runAsync(): RunAsync

    fun clearViewModel(): ClearViewModel

    fun provideResources(): ProvideResources

    fun context(): Context

    class Base(private val context: Context, private val clearViewModel: ClearViewModel) : Core {

        private val navigation: Navigation.Mutable = Navigation.Base()
        private val runAsync = RunAsync.Base()
        private val provideResources: ProvideResources = BaseProvideResources(context)

        override fun navigation() = navigation

        override fun runAsync() = runAsync

        override fun clearViewModel() = clearViewModel

        override fun provideResources() = provideResources

        override fun context() = context
    }
}