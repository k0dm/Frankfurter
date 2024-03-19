package com.example.presentation.settings

import com.example.presentation.core.LiveDataWrapper
import javax.inject.Inject

interface SettingsCommunication : LiveDataWrapper<SettingsUiState> {

    class Base @Inject constructor() : SettingsCommunication,
        LiveDataWrapper.Single<SettingsUiState>()
}