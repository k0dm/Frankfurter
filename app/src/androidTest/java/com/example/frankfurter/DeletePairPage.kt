package com.example.frankfurter

import android.widget.Button
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.example.presentation.R
import org.hamcrest.CoreMatchers.allOf

class DeletePairPage {

    private val rootId: Int = R.id.deleteLayout
    private val rootLayout = onView(
        allOf(
            withId(rootId),
            isAssignableFrom(LinearLayout::class.java),
        )
    )

    fun checkVisible() = rootLayout.check(matches(isDisplayed()))

    fun checkNotVisible() = rootLayout.check(doesNotExist())

    fun clickYes() = onView(
        allOf(
            withId(R.id.yesButton),
            isAssignableFrom(Button::class.java),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    ).perform(click())

    fun clickNo() = onView(
        allOf(
            withId(R.id.noButton),
            isAssignableFrom(Button::class.java),
            withParent(isAssignableFrom(LinearLayout::class.java))
        )
    ).perform(click())

}
