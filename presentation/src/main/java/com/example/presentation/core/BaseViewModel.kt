package com.example.presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel(private val runAsync: RunAsync) : CustomViewModel {

    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    protected fun <T : Any> runAsync(
        backgroundBlock: suspend () -> T,
        uiBlock: (T) -> Unit
    ) {
        runAsync.start(viewModelScope, backgroundBlock, uiBlock)
    }
}