package com.example.presentation.loadingcurrencies

import com.example.presentation.core.views.ChangeVisibility
import com.example.presentation.core.views.ErrorText

interface LoadingCurrenciesUiState {

    fun show(progressBar: ChangeVisibility, errorTextView: ErrorText, retryButton: ChangeVisibility)

    object Loading : LoadingCurrenciesUiState {

        override fun show(
            progressBar: ChangeVisibility,
            errorTextView: ErrorText,
            retryButton: ChangeVisibility
        ) {
            progressBar.show()
            errorTextView.hide()
            retryButton.hide()
        }
    }

    data class Error(private val message: String) : LoadingCurrenciesUiState {

        override fun show(
            progressBar: ChangeVisibility,
            errorTextView: ErrorText,
            retryButton: ChangeVisibility
        ) {
            progressBar.hide()
            errorTextView.show()
            errorTextView.changeText(message)
            retryButton.show()
        }
    }

    object Empty : LoadingCurrenciesUiState {
        override fun show(
            progressBar: ChangeVisibility,
            errorTextView: ErrorText,
            retryButton: ChangeVisibility
        ) = Unit
    }
}