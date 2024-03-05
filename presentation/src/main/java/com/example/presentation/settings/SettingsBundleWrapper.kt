package com.example.presentation.settings

import android.os.Bundle

interface SettingsBundleWrapper {

    fun isEmpty(): Boolean

    fun save(from: String, to: String)

    fun restore(): Pair<String, String>

    class Base(private val bundle: Bundle?) : SettingsBundleWrapper {

        override fun isEmpty() = bundle == null

        override fun save(from: String, to: String) = with(bundle!!) {
            putString(FROM_KEY, from)
            putString(TO_KEY, to)
        }

        override fun restore() = with(bundle!!) {
            Pair(getString(FROM_KEY) ?: "", getString(TO_KEY) ?: "")
        }

        companion object {
            private const val FROM_KEY = "FROM_KEY"
            private const val TO_KEY = "TO_KEY"
        }
    }
}