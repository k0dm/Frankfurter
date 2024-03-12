package com.example.presentation.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.example.presentation.core.BaseFragment
import com.example.presentation.databinding.FragmentSubscriptionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>() {

    private val viewModel: SubscriptionViewModel by viewModels()

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSubscriptionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.comeback()
                }
            }
        )

        binding.goToSettingsImageButton.setOnClickListener {
            viewModel.comeback()
        }

        binding.buyButton.setOnClickListener {
            viewModel.buyPremium()
        }
    }
}