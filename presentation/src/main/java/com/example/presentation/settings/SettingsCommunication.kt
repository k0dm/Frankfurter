package com.example.presentation.settings

import com.example.presentation.core.LiveDataWrapper

interface SettingsCommunication : LiveDataWrapper<SettingsUiState> {

    class Base : SettingsCommunication, LiveDataWrapper.Single<SettingsUiState>()
}