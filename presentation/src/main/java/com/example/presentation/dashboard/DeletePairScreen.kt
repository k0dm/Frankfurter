package com.example.presentation.dashboard

import androidx.fragment.app.FragmentManager
import com.example.presentation.main.Screen

data class DeletePairScreen(
    private val fromCurrency: String,
    private val toCurrency: String,
) : Screen {

    override fun show(containerId: Int, supportFragmentManager: FragmentManager) {
        DeleteBottomSheetDialogFragment.newInstance(fromCurrency, toCurrency)
            .show(supportFragmentManager, DeleteBottomSheetDialogFragment::class.java.simpleName)
    }
}