package com.example.presentation.dashboard

import com.example.presentation.core.LiveDataWrapper

interface DashboardCommunication : LiveDataWrapper<DashboardUiState> {

    class Base : DashboardCommunication, LiveDataWrapper.Single<DashboardUiState>()
}