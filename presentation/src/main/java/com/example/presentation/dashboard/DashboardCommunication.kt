package com.example.presentation.dashboard

import com.example.presentation.core.LiveDataWrapper
import javax.inject.Inject
import javax.inject.Singleton

interface DashboardCommunication : LiveDataWrapper<DashboardUiState> {

    @Singleton
    class Base @Inject constructor() : DashboardCommunication,
        LiveDataWrapper.Single<DashboardUiState>()
}