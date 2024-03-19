package com.example.presentation.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.presentation.databinding.ViewholderCurrencyBinding
import com.example.presentation.databinding.ViewholderNoMoreCurrenciesBinding

interface TypeUi {

    fun viewHolder(parent: ViewGroup, chooseCurrencyBlock: (String) -> Unit): CurrencyViewHolder

    object Currency : TypeUi {

        override fun viewHolder(parent: ViewGroup, chooseCurrencyBlock: (String) -> Unit) =
            CurrencyViewHolder.Currency(
                ViewholderCurrencyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                chooseCurrencyBlock
            )
    }

    object Empty : TypeUi {

        override fun viewHolder(parent: ViewGroup, chooseCurrencyBlock: (String) -> Unit) =
            CurrencyViewHolder.Empty(
                ViewholderNoMoreCurrenciesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}