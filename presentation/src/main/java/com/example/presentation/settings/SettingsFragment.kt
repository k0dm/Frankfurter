package com.example.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.example.presentation.core.BaseFragment
import com.example.presentation.databinding.FragmentSettingsBinding
import com.example.presentation.settings.adapter.CurrencyAdapter

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by activityViewModels()
    private lateinit var currencyFromAdapter: CurrencyAdapter
    private lateinit var currencyToAdapter: CurrencyAdapter

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyFromAdapter = CurrencyAdapter { currency ->
            viewModel.chooseFrom(currency)
        }
        currencyToAdapter = CurrencyAdapter { currency ->
            viewModel.chooseTo(currencyFromAdapter.selected(), currency)
        }
        binding.fromCurrencyRecyclerView.adapter = currencyFromAdapter
        binding.toCurrencyRecyclerView.adapter = currencyToAdapter

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.goToDashboard()
                }
            })

        binding.saveButton.setOnClickListener {
            viewModel.save(currencyFromAdapter.selected(), currencyToAdapter.selected())
        }

        binding.backImageButton.setOnClickListener {
            viewModel.goToDashboard()
        }

        viewModel.liveData().observe(viewLifecycleOwner) { settingsUiState ->
            settingsUiState.show(currencyFromAdapter, currencyToAdapter)
            settingsUiState.show(binding.saveButton)
        }

        viewModel.init(SettingsBundleWrapper.Base(savedInstanceState))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        SettingsBundleWrapper.Base(outState)
            .save(currencyFromAdapter.selected(), currencyToAdapter.selected())
    }
}

