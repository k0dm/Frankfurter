package com.example.presentation.main

import androidx.lifecycle.ViewModel
import com.example.presentation.core.ProvideLiveData
import com.example.presentation.dashboard.DashboardScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigation: Navigation.Mutable
) : ViewModel(), ProvideLiveData<Screen> {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            navigation.updateUi(DashboardScreen)
        }
    }

    override fun liveData() = navigation.liveData()
}

