package com.example.frankfurter

import androidx.lifecycle.ViewModel
import com.example.presentation.core.ProvideViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val viewModelProvider: ProvideViewModel) : ProvideViewModel {

    private val viewModelStore = mutableMapOf<Class<out ViewModel>, ViewModel>()

    override fun <T : ViewModel> viewModel(clazz: Class<out T>): T {
        return if (viewModelStore.containsKey(clazz)) {
            viewModelStore[clazz]
        } else {
            val viewModel = viewModelProvider.viewModel(clazz)
            viewModelStore[clazz] = viewModel
            viewModel
        } as T
    }
}