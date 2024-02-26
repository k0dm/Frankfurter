package com.example.frankfurter

import android.content.Context
import com.example.data.core.ProvideResources

class BaseProvideResources(private val context: Context) : ProvideResources {

    private fun string(id: Int) =
        context.resources.getString(id)

    override fun noInternetConnectionMessage() = string(R.string.no_internet_connection)

    override fun serviceUnavailableMessage() = string(R.string.service_unavailable)
}