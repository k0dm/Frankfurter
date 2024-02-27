package com.example.frankfurter

import com.example.presentation.core.ClearViewModel
import com.example.presentation.core.CustomViewModel
import com.example.presentation.core.ProvideViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val viewModelProvider: ProvideViewModel) : ProvideViewModel,
    ClearViewModel {

    private val viewModelStore = mutableMapOf<Class<out CustomViewModel>, CustomViewModel>()

    override fun <T : CustomViewModel> viewModel(clazz: Class<out T>): T {
        return if (viewModelStore.containsKey(clazz)) {
            viewModelStore[clazz]
        } else {
            val viewModel = viewModelProvider.viewModel(clazz)
            viewModelStore[clazz] = viewModel
            viewModel
        } as T
    }

    override fun clear(clazz: Class<out CustomViewModel>) {
        viewModelStore.remove(clazz)
    }
}