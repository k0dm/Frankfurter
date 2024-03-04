package com.example.presentation.settings.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.core.UpdateAdapter
import com.example.presentation.databinding.ViewholderCurrencyBinding
import com.example.presentation.databinding.ViewholderNoMoreCurrenciesBinding
import com.example.presentation.settings.CurrencyUi

class CurrencyAdapter(
    private val types: List<TypeUi> = listOf(TypeUi.Currency, TypeUi.Empty),
    private val chooseCurrencyBlock: (String) -> Unit
) : RecyclerView.Adapter<CurrencyViewHolder>(), UpdateAdapter<CurrencyUi> {

    private val currencies = mutableListOf<CurrencyUi>()

    override fun update(newList: List<CurrencyUi>) {
        val diffResult = DiffUtil.calculateDiff(CurrencyDiffUtil(currencies, newList))
        currencies.clear()
        currencies.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun selected(): String = currencies.find { it.isSelected() }?.value() ?: ""

    override fun getItemCount() = currencies.size

    override fun getItemViewType(position: Int) = types.indexOf(currencies[position].type())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        types[viewType].viewHolder(parent, chooseCurrencyBlock)

    override fun onBindViewHolder(viewHolder: CurrencyViewHolder, position: Int) =
        viewHolder.bind(currencies[position])
}

abstract class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(currencyUi: CurrencyUi)

    class Currency(
        private val binding: ViewholderCurrencyBinding,
        private val chooseCurrencyBlock: (String) -> Unit
    ) : CurrencyViewHolder(binding.root) {

        override fun bind(currencyUi: CurrencyUi) {
            currencyUi.show(binding.currencyTextView, binding.chosenImageView)
            binding.currencyLayout.setOnClickListener {
                chooseCurrencyBlock.invoke(binding.currencyTextView.text.toString())
            }
        }
    }

    class Empty(binding: ViewholderNoMoreCurrenciesBinding) : CurrencyViewHolder(binding.root) {
        override fun bind(currencyUi: CurrencyUi) = Unit
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