package com.example.frankfurter

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
import com.example.presentation.core.views.CustomButton
import org.hamcrest.CoreMatchers.allOf

class LoadingPage {
    private val rootId: Int = R.id.loadingLayout
    private val rootLayout = onView(
        allOf(
            withId(rootId),
            isAssignableFrom(LinearLayout::class.java)
        )
    )

    fun checkVisible() = rootLayout.check(matches(isDisplayed()))

    fun clickRetry() = onView(
        allOf(
            withId(R.id.retryButton),
            isAssignableFrom(CustomButton::class.java),
            withParent(withId(rootId))
        )
    ).perform(click())

    fun checkNotVisible() = rootLayout.check(doesNotExist())
}
