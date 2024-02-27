package com.example.frankfurter

import android.app.Application
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.CustomViewModel
import com.example.presentation.core.ProvideViewModel

class FrankfurterApp : Application(), ProvideViewModel, ClearViewModel {

    private lateinit var factory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()
        factory = ViewModelFactory(
            BaseProvideViewModel(
                ProvideModule.Base(Core.Base(this, this))
            )
        )
    }

    override fun <T : CustomViewModel> viewModel(clazz: Class<out T>) = factory.viewModel(clazz)

    override fun clear(clazz: Class<out CustomViewModel>) {
        factory.clear(clazz)
    }
}