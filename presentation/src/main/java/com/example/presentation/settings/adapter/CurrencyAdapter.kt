package com.example.presentation.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.core.UpdateAdapter
import com.example.presentation.databinding.ViewholderCurrencyBinding
import com.example.presentation.settings.CurrencyUi

class CurrencyAdapter(
    private val chooseCurrencyBlock: (String) -> Unit
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>(), UpdateAdapter<CurrencyUi> {

    private val currencies = mutableListOf<CurrencyUi>()

    override fun update(newList: List<CurrencyUi>) {
        val diffResult = DiffUtil.calculateDiff(CurrencyDiffUtil(currencies, newList))
        currencies.clear()
        currencies.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = currencies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CurrencyViewHolder(
        ViewholderCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(viewHolder: CurrencyViewHolder, position: Int) {
        viewHolder.bind(currencies[position])
    }

    inner class CurrencyViewHolder(
        private val binding: ViewholderCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currencyUi: CurrencyUi) {
            currencyUi.show(binding.currencyTextView, binding.chosenImageView)
            binding.currencyLayout.setOnClickListener {
                chooseCurrencyBlock.invoke(binding.currencyTextView.text.toString())
            }
        }
    }
}

private class CurrencyDiffUtil(
    private val oldList: List<CurrencyUi>,
    private val newList: List<CurrencyUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].areItemsTheSame(newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
