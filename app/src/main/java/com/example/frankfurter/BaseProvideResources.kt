package com.example.frankfurter

import android.content.Context
import com.example.data.core.ProvideResources
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseProvideResources @Inject constructor(
    @ApplicationContext private val context: Context
) : ProvideResources {

    private fun string(id: Int) = context.resources.getString(id)

    override fun noInternetConnectionMessage() = string(R.string.no_internet_connection)

    override fun serviceUnavailableMessage() = string(R.string.service_unavailable)
}