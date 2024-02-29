package com.example.presentation.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.presentation.dashboard.RetryClickAction
import com.example.presentation.databinding.ViewholderCurrencyPairBinding
import com.example.presentation.databinding.ViewholderEmptyBinding
import com.example.presentation.databinding.ViewholderErrorBinding
import com.example.presentation.databinding.ViewholderProgressBinding

interface DashboardTypeUi {

    fun viewHolder(viewGroup: ViewGroup, viewModel: RetryClickAction): DashboardViewHolder

    object Empty : DashboardTypeUi {

        override fun viewHolder(viewGroup: ViewGroup, viewModel: RetryClickAction) =
            DashboardViewHolder.Empty(
                ViewholderEmptyBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
    }

    object Progress : DashboardTypeUi {

        override fun viewHolder(viewGroup: ViewGroup, viewModel: RetryClickAction) =
            DashboardViewHolder.Progress(
                ViewholderProgressBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
    }

    object Error : DashboardTypeUi {

        override fun viewHolder(viewGroup: ViewGroup, viewModel: RetryClickAction) =
            DashboardViewHolder.Error(
                ViewholderErrorBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
                viewModel
            )
    }

    object CurrencyPair : DashboardTypeUi {

        override fun viewHolder(viewGroup: ViewGroup, viewModel: RetryClickAction) =
            DashboardViewHolder.CurrencyPair(
                ViewholderCurrencyPairBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
    }
}