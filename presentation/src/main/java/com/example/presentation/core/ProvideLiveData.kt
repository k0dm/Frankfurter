package com.example.presentation.core

import androidx.lifecycle.LiveData

interface ProvideLiveData<T : Any> {

    fun liveData(): LiveData<T> = throw IllegalStateException("Don't use in Unit Test")
}