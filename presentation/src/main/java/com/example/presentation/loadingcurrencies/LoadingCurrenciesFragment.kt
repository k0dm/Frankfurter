package com.example.presentation.loadingcurrencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.presentation.core.BaseFragment
import com.example.presentation.databinding.FragmentLoadingCurrenciesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingCurrenciesFragment : BaseFragment<FragmentLoadingCurrenciesBinding>() {

    private val viewModel: LoadingCurrenciesViewModel by viewModels()

    override fun inflate(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentLoadingCurrenciesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData().observe(viewLifecycleOwner) { uiState ->
            uiState.show(binding.progressBar, binding.errorText, binding.retryButton)
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadCurrencies()
        }

        viewModel.init(isFirstRun = savedInstanceState == null)
    }
}