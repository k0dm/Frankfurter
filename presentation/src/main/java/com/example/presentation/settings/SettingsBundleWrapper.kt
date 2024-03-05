package com.example.presentation.settings

import android.os.Bundle

interface SettingsBundleWrapper {

    fun init(viewModel: SettingsViewModel)

    fun save(from: String, to: String)

    class Base(private val bundle: Bundle?) : SettingsBundleWrapper {

        override fun save(from: String, to: String) = with(bundle!!) {
            putString(FROM_KEY, from)
            putString(TO_KEY, to)
        }

        override fun init(viewModel: SettingsViewModel) {
            if (bundle == null) {
                viewModel.init()
            } else {
                val from = bundle.getString(FROM_KEY)
                val to = bundle.getString(TO_KEY)
                if (!from.isNullOrBlank()) {
                    viewModel.chooseFrom(from)
                    if (!to.isNullOrBlank()) {
                        viewModel.chooseTo(from, to)
                    }
                }
            }
        }

        companion object {
            private const val FROM_KEY = "FROM_KEY"
            private const val TO_KEY = "TO_KEY"
        }
    }
}