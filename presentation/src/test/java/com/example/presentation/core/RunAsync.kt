package com.example.presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

@Suppress("UNCHECKED_CAST")
internal class FakeRunAsync : RunAsync {

    private var cacheResult = Any()
    private var cacheUiBlock: (Any) -> Unit = {}

    override fun <T : Any> start(
        coroutineScope: CoroutineScope,
        backgroundBlock: suspend () -> T,
        uiBlock: (T) -> Unit
    ) = runBlocking {
        cacheResult = backgroundBlock.invoke()
        cacheUiBlock = uiBlock as (Any) -> Unit
    }

    fun pingResult() {
        cacheUiBlock.invoke(cacheResult)
    }
}