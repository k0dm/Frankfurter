package com.example.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.presentation.core.BaseFragment
import com.example.presentation.dashboard.adapter.DashboardAdapter
import com.example.presentation.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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