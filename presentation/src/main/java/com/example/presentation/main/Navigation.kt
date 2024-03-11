package com.example.presentation.main

import com.example.presentation.core.LiveDataWrapper
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.UpdateUi
import javax.inject.Inject
import javax.inject.Singleton

interface Navigation {

    interface Update : UpdateUi<Screen>

    interface Mutable: Update, ProvideLiveData<Screen>

    @Singleton
    class Base @Inject constructor() : Mutable, LiveDataWrapper.Single<Screen>()
}