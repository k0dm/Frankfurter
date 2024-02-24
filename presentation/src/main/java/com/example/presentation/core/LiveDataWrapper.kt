package com.example.presentation.core

import androidx.lifecycle.MutableLiveData

interface LiveDataWrapper<T : Any> : UpdateUi<T>, ProvideLiveData<T> {

    abstract class Abstract<T : Any> : LiveDataWrapper<T> {

        private val liveData = MutableLiveData<T>()

        override fun updateUi(value: T) {
            liveData.value = value
        }

        override fun liveData() = liveData
    }
}