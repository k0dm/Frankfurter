package com.example.presentation.dashboard

import androidx.fragment.app.FragmentManager
import com.example.presentation.main.Screen

class DeletePairScreen(
    private val fromCurrency: String,
    private val toCurrency: String,
    private val function: () -> Unit,
) : Screen {

    override fun show(containerId: Int, supportFragmentManager: FragmentManager) {
        DeleteBottomSheetFragment.newInstance(fromCurrency, toCurrency, function)
            .show(supportFragmentManager, DeleteBottomSheetFragment::class.java.simpleName)
    }
}