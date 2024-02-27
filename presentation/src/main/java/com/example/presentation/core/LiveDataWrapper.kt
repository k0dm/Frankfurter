package com.example.presentation.core

import androidx.lifecycle.MutableLiveData

interface LiveDataWrapper<T : Any> : UpdateUi<T>, ProvideLiveData<T> {

    abstract class Abstract<T : Any> : LiveDataWrapper<T> {

        protected abstract val liveData: MutableLiveData<T>

        override fun updateUi(value: T) {
            liveData.value = value
        }

        override fun liveData() = liveData
    }

    abstract class Single<T : Any> : Abstract<T>() {
        override val liveData: MutableLiveData<T> = SingleLiveEvent()
    }

    abstract class Base<T : Any> : Abstract<T>() {
        override val liveData: MutableLiveData<T> = MutableLiveData()
    }
}