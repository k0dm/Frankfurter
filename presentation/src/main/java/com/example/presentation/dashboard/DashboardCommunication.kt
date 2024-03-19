package com.example.presentation.dashboard

import com.example.presentation.core.LiveDataWrapper
import javax.inject.Inject

interface DashboardCommunication : LiveDataWrapper<DashboardUiState> {

    class Base @Inject constructor() : DashboardCommunication,
        LiveDataWrapper.Single<DashboardUiState>()
}