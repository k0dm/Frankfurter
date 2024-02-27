package com.example.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.presentation.core.BaseFragment
import com.example.presentation.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

}