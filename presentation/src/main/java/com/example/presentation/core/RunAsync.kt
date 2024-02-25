package com.example.presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {

    fun <T : Any> start(
        coroutineScope: CoroutineScope,
        backgroundBlock: suspend () -> T,
        uiBlock: (T) -> Unit
    )

    class Base: RunAsync {

        override fun <T : Any> start(
            coroutineScope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                val result = backgroundBlock.invoke()
                withContext(Dispatchers.Main) {
                    uiBlock.invoke(result)
                }
            }
        }
    }
}