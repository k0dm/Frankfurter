package com.example.presentation.dashboard

import androidx.fragment.app.FragmentManager
import com.example.presentation.main.Screen

class DeletePairScreen(
    private val fromCurrency: String,
    private val toCurrency: String,
) : Screen {

    override fun show(containerId: Int, supportFragmentManager: FragmentManager) {
        DeleteBottomSheetFragment.newInstance(fromCurrency, toCurrency)
            .show(supportFragmentManager, DeleteBottomSheetFragment::class.java.simpleName)
    }
}