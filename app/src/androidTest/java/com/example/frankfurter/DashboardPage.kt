package com.example.frankfurter

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.presentation.R
import org.hamcrest.CoreMatchers.allOf

class DashboardPage {

    private val rootId: Int = R.id.dashboardLayout

    private val rootLayout = onView(
        allOf(
            withId(rootId),
            isAssignableFrom(LinearLayout::class.java)
        )
    )

    fun checkVisible() = rootLayout.check(matches(isDisplayed()))

    fun checkNotVisible() = rootLayout.check(doesNotExist())

    private val recyclerViewId = R.id.favoritePairsRecyclerView

    fun checkNoAddedPairs() =
        onView(RecyclerViewMatcher(position = 0, R.id.messageTextView, recyclerViewId))
            .check(matches(withText("Go to settings and add new pair")))

    fun checkPair(position: Int, currencyPair: String, rates: String) {
        onView(RecyclerViewMatcher(position = position, R.id.currencyPairTextView, recyclerViewId))
            .check(matches(withText(currencyPair)))
        onView(RecyclerViewMatcher(position = position, R.id.ratesTextView, recyclerViewId))
            .check(matches(withText(rates)))
    }

    fun clickAtPair(position: Int) =
        onView(RecyclerViewMatcher(position = position, recyclerViewId = recyclerViewId))
            .perform(click())


    fun goToSettings() = onView(
        allOf(
            withId(R.id.settingsImageView),
            isAssignableFrom(ImageView::class.java),
            withParent(withId(rootId))
        )
    ).run {
        check(matches(DrawableMatcher(R.drawable.settings_icon)))
        perform(click())
    }

    fun checkError(message: String) {
        onView(RecyclerViewMatcher(position = 0, R.id.errorTextView, recyclerViewId))
            .check(matches(withText(message)))
    }

    fun clickRetry() {
        onView(RecyclerViewMatcher(position = 0, R.id.retryButton, recyclerViewId))
            .perform(click())
    }
}