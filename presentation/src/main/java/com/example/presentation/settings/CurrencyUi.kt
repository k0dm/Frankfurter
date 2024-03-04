package com.example.presentation.settings

import com.example.presentation.core.views.ChangeText
import com.example.presentation.core.views.ChangeVisibility
import com.example.presentation.settings.adapter.TypeUi

interface CurrencyUi {

    fun type(): TypeUi

    fun areItemsTheSame(currencyUi: CurrencyUi): Boolean

    fun show(currencyTextView: ChangeText, chosenImageView: ChangeVisibility) = Unit

    fun isSelected(): Boolean = false

    fun value(): String = ""

    data class Base(private val value: String, private val chosen: Boolean = false) : CurrencyUi {

        override fun show(currencyTextView: ChangeText, chosenImageView: ChangeVisibility) {
            currencyTextView.changeText(value)
            chosenImageView.run { if (chosen) show() else hide() }
        }

        override fun isSelected() = chosen

        override fun value() = value

        override fun areItemsTheSame(currencyUi: CurrencyUi) =
            currencyUi.value() == this.value


        override fun type() = TypeUi.Currency
    }

    object Empty : CurrencyUi {

        override fun areItemsTheSame(currencyUi: CurrencyUi) = currencyUi == this

        override fun type() = TypeUi.Empty
    }
}