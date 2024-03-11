package com.example.presentation.settings

import com.example.presentation.core.LiveDataWrapper
import javax.inject.Inject
import javax.inject.Singleton

interface SettingsCommunication : LiveDataWrapper<SettingsUiState> {

    @Singleton
    class Base @Inject constructor() : SettingsCommunication,
        LiveDataWrapper.Single<SettingsUiState>()
}