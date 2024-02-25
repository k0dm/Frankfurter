package com.example.presentation.loadingcurrencies

import android.view.View
import com.example.presentation.databinding.FragmentLoadingCurrenciesBinding

interface LoadingCurrenciesUiState {

    fun show(binding: FragmentLoadingCurrenciesBinding)

    object Loading : LoadingCurrenciesUiState {

        override fun show(binding: FragmentLoadingCurrenciesBinding) = with(binding) {
            progressBar.visibility = View.VISIBLE
            errorText.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : LoadingCurrenciesUiState {

        override fun show(binding: FragmentLoadingCurrenciesBinding) = with(binding) {
            progressBar.visibility = View.GONE
            errorText.visibility = View.VISIBLE
            errorText.text = message
            retryButton.visibility = View.VISIBLE
        }
    }

    object Empty : LoadingCurrenciesUiState {
        override fun show(binding: FragmentLoadingCurrenciesBinding) = Unit
    }
}