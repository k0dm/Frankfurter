package com.example.frankfurter.modules

import com.example.presentation.core.CustomViewModel

interface Module<T : CustomViewModel> {

    fun viewModel(): T
}

