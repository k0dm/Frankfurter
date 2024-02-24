package com.example.presentation.core

import androidx.lifecycle.ViewModel

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(clazz: Class<out T>): T
}