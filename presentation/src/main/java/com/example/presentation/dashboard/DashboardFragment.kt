package com.example.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.core.BaseFragment
import com.example.presentation.dashboard.adapter.DashboardAdapter
import com.example.presentation.databinding.FragmentDashboardBinding
import com.google.android.material.snackbar.Snackbar

class DashboardFragment :
    BaseFragment<FragmentDashboardBinding, DashboardViewModel>(DashboardViewModel::class.java) {

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DashboardAdapter(viewModel)
        binding.favoritePairsRecyclerView.adapter = adapter

        ItemTouchHelper(object : SimpleCallback(0, ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.remove(viewHolder.adapterPosition)
                Snackbar.make(binding.root, "Pair deleted", Snackbar.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(binding.favoritePairsRecyclerView)

        binding.settingsImageView.setOnClickListener {
            viewModel.goToSettings()
        }

        viewModel.liveData().observe(viewLifecycleOwner) { dashboardUiState ->
            dashboardUiState.show(adapter)
        }

        viewModel.init()
    }
}