package com.example.frankfurter.modules

import com.example.frankfurter.Core
import com.example.presentation.main.MainViewModel

class MainModule(private val core: Core) : Module<MainViewModel> {

    override fun viewModel() = MainViewModel(navigation = core.navigation())
}