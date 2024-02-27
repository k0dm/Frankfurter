package com.example.presentation.loadingcurrencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.core.BaseFragment
import com.example.presentation.core.ProvideViewModel
import com.example.presentation.databinding.FragmentLoadingCurrenciesBinding

class LoadingCurrenciesFragment : BaseFragment<FragmentLoadingCurrenciesBinding>() {

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoadingCurrenciesBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel =
            (activity as ProvideViewModel).viewModel(LoadingCurrenciesViewModel::class.java)

        viewModel.liveData().observe(viewLifecycleOwner) { uiState ->
            uiState.show(binding.progressBar, binding.errorText, binding.retryButton)
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadCurrencies()
        }

        viewModel.init(isFirstRun = savedInstanceState == null)
    }
}