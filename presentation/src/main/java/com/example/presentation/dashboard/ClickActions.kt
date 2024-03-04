package com.example.presentation.dashboard

interface ClickActions {

    fun retry()

    fun openDeletePairDialog(from: String, to: String, function: () -> Unit)
}