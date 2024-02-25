package com.example.presentation.loadingcurrencies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.presentation.core.ProvideViewModel
import com.example.presentation.databinding.FragmentLoadingCurrenciesBinding

class LoadingCurrenciesFragment : Fragment() {

    private var _binding: FragmentLoadingCurrenciesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingCurrenciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel =
            (activity as ProvideViewModel).viewModel(LoadingCurrenciesViewModel::class.java)

        viewModel.liveData().observe(viewLifecycleOwner) { uiState ->
            uiState.show(binding)
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadCurrencies()
        }

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}