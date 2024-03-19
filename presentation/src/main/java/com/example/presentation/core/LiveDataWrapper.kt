package com.example.presentation.core

import androidx.lifecycle.MutableLiveData

interface LiveDataWrapper<T : Any> : UpdateUi<T>, ProvideLiveData<T> {

    abstract class Abstract<T : Any>(
        protected val liveData: MutableLiveData<T>
    ) : LiveDataWrapper<T> {

        override fun updateUi(value: T) {
            liveData.value = value
        }

        override fun liveData() = liveData
    }

    abstract class Single<T : Any>(
        liveData: MutableLiveData<T> = SingleLiveEvent()
    ) : Abstract<T>(liveData)
}