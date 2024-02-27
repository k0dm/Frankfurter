package com.example.frankfurter

import android.app.Application
import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.CustomViewModel
import com.example.presentation.core.ProvideViewModel

class FrankfurterApp : Application(), ProvideViewModel {

    private lateinit var factory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()
        val clearViewModel = object : ClearViewModel {
            override fun clear(clazz: Class<out CustomViewModel>) = factory.clear(clazz)
        }
        val core = Core.Base(this, clearViewModel)
        factory = ViewModelFactory(BaseProvideViewModel(ProvideModule.Base(core)))
    }

    override fun <T : CustomViewModel> viewModel(clazz: Class<out T>) = factory.viewModel(clazz)
}