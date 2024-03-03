package com.example.presentation.dashboard.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.core.UpdateAdapter
import com.example.presentation.dashboard.ClickActions
import com.example.presentation.databinding.ViewholderCurrencyPairBinding
import com.example.presentation.databinding.ViewholderEmptyBinding
import com.example.presentation.databinding.ViewholderErrorBinding
import com.example.presentation.databinding.ViewholderProgressBinding

class DashboardAdapter(
    private val viewModel: ClickActions,
    private val types: List<DashboardTypeUi> =
        listOf(
            DashboardTypeUi.Empty,
            DashboardTypeUi.Progress,
            DashboardTypeUi.Error,
            DashboardTypeUi.CurrencyPair
        )
) : RecyclerView.Adapter<DashboardViewHolder>(), UpdateAdapter<DashboardCurrencyPairUi> {

    private val pairUiList = mutableListOf<DashboardCurrencyPairUi>()

    override fun update(newList: List<DashboardCurrencyPairUi>) {
        val diffResult = DiffUtil.calculateDiff(DashboardDiffUtilCallback(pairUiList, newList))
        pairUiList.clear()
        pairUiList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun remove(position: Int) {
        pairUiList[position].remove(viewModel)
    }

    override fun getItemCount() = pairUiList.size

    override fun getItemViewType(position: Int) =
        types.indexOf(pairUiList[position].type())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        types[viewType].viewHolder(parent, viewModel)

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) =
        holder.bind(pairUiList[position])
}

abstract class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(dashboardItem: DashboardCurrencyPairUi) = Unit
    class Empty(binding: ViewholderEmptyBinding) : DashboardViewHolder(binding.root)

    class Progress(binding: ViewholderProgressBinding) : DashboardViewHolder(binding.root)

    class Error(
        private val binding: ViewholderErrorBinding,
        private val viewModel: ClickActions
    ) : DashboardViewHolder(binding.root) {

        override fun bind(dashboardItem: DashboardCurrencyPairUi) {
            dashboardItem.showError(binding.errorTextView)
            binding.retryButton.setOnClickListener { viewModel.retry() }
        }
    }

    class CurrencyPair(
        private val binding: ViewholderCurrencyPairBinding
    ) : DashboardViewHolder(binding.root) {

        override fun bind(dashboardItem: DashboardCurrencyPairUi) {
            dashboardItem.showCurrencyPair(binding.currencyPairTextView, binding.ratesTextView)
        }
    }
}

private class DashboardDiffUtilCallback(
    private val oldList: List<DashboardCurrencyPairUi>,
    private val newList: List<DashboardCurrencyPairUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        areItemsTheSame(oldItemPosition, newItemPosition)
}