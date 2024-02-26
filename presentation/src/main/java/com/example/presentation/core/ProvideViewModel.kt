package com.example.presentation.core


interface ProvideViewModel {

    fun <T : CustomViewModel> viewModel(clazz: Class<out T>): T
}