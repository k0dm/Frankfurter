package com.example.presentation.dashboard

import android.util.Log
import com.example.presentation.core.LiveDataWrapper
import javax.inject.Inject

interface DashboardCommunication : LiveDataWrapper<DashboardUiState> {

    class Base @Inject constructor() : DashboardCommunication,
        LiveDataWrapper.Single<DashboardUiState>() {
        init {
            Log.d("k0dm", "DashboardCommunication ${hashCode()}")
        }
    }
}