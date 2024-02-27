package com.example.frankfurter

import com.example.presentation.core.CustomViewModel
import com.example.presentation.core.ProvideViewModel

class BaseProvideViewModel(private val provideModule: ProvideModule) : ProvideViewModel {

    override fun <T : CustomViewModel> viewModel(clazz: Class<out T>) =
        provideModule.module(clazz).viewModel()
}