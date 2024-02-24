package com.example.presentation.main

import com.example.presentation.core.LiveDataWrapper
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.core.UpdateUi

interface Navigation {

    interface Update : UpdateUi<Screen>

    interface Mutable: Update, ProvideLiveData<Screen>

    class Base : Mutable, LiveDataWrapper.Abstract<Screen>()
}