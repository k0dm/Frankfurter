package com.example.frankfurter

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.presentation.core.ProvideViewModel

class FrankfurterApp : Application(), ProvideViewModel {

    private lateinit var factory: ProvideViewModel

    override fun onCreate() {
        super.onCreate()
        factory = ViewModelFactory(BaseProvideViewModel(this))
    }

    override fun <T : ViewModel> viewModel(clazz: Class<out T>) = factory.viewModel(clazz)
}