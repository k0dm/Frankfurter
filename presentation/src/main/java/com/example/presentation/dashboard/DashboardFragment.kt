package com.example.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.presentation.core.BaseFragment
import com.example.presentation.dashboard.adapter.DashboardAdapter
import com.example.presentation.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = viewModel(DashboardViewModel::class.java)

        val adapter = DashboardAdapter(viewModel)
        binding.favoritePairsRecyclerView.adapter = adapter

        binding.settingsImageView.setOnClickListener {
            viewModel.goToSettings()
        }

        viewModel.liveData().observe(viewLifecycleOwner) { dashboardUiState ->
            dashboardUiState.show(adapter)
        }

        viewModel.init()
    }
}