package com.example.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.presentation.core.BaseFragment
import com.example.presentation.databinding.FragmentSettingsBinding
import com.example.presentation.settings.adapter.CurrencyAdapter

class SettingsFragment
    : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(SettingsViewModel::class.java) {

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currencyFromAdapter = CurrencyAdapter { currency ->
            viewModel.chooseFrom(currency)
        }
        val currencyToAdapter = CurrencyAdapter { currency ->
            viewModel.chooseTo(currency)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.goToDashboard()
                }
            })

        viewModel.liveData().observe(viewLifecycleOwner) { settingsUiState ->
            settingsUiState.show(currencyFromAdapter, currencyToAdapter)
        }

        viewModel.init()
    }
}