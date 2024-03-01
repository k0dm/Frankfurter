package com.example.presentation.settings

import com.example.presentation.core.views.ChangeText
import com.example.presentation.core.views.ChangeVisibility

data class CurrencyUi(private val value: String, private val chosen: Boolean = false) {

    fun areItemsTheSame(currencyUi: CurrencyUi) = currencyUi.value == this.value

    fun show(currencyTextView: ChangeText, chosenImageView: ChangeVisibility) {
        currencyTextView.changeText(value)
        chosenImageView.also { if (chosen) it.show() else it.hide() }
    }
}