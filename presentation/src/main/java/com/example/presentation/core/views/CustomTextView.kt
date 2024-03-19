package com.example.presentation.core.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet

class CustomTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr), ErrorText {

    init {
        freezesText = true
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let {
            val state = VisibilitySavedState(it)
            state.save(this)
            return state
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoreState = state as VisibilitySavedState?
        super.onRestoreInstanceState(restoreState?.superState)
        restoreState?.restore(this)
    }

    override fun show() {
        visibility = VISIBLE
    }

    override fun hide() {
        visibility = GONE
    }

    override fun changeText(text: String) {
        this.text = text
    }
}

interface ErrorText : ChangeVisibility, ChangeText