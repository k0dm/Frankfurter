package com.example.frankfurter

import android.widget.ImageButton
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
import com.example.presentation.core.views.CustomButton
import org.hamcrest.CoreMatchers.allOf

class SettingsPage {

    private val rootId: Int = R.id.settingsLayout
    private val rootLayout = onView(
        allOf(
            withId(rootId),
            isAssignableFrom(LinearLayout::class.java)
        )
    )

    fun checkVisible() = rootLayout.check(matches(isDisplayed()))

    fun checkNotVisible() = rootLayout.check(doesNotExist())

    private val fromCurrencyRecyclerView: Int = R.id.fromCurrencyRecyclerView

    fun checkFromCurrencies(vararg currencies: String) = currencies.forEachIndexed { i, currency ->
        onView(
            RecyclerViewMatcher(
                position = i,
                targetViewId = R.id.currencyTextView,
                recyclerViewId = fromCurrencyRecyclerView
            )
        ).check(matches(withText(currency)))
    }

    fun chooseFrom(position: Int) = onView(
        RecyclerViewMatcher(position = position, recyclerViewId = fromCurrencyRecyclerView)
    ).perform(click())

    fun checkChosenFrom(position: Int) = onView(
        RecyclerViewMatcher(
            position = position,
            targetViewId = R.id.chosenImageView,
            recyclerViewId = fromCurrencyRecyclerView
        )
    ).run {
        check(matches(DrawableMatcher(R.drawable.check)))
        check(matches(isDisplayed()))
    }

    private val toCurrencyRecyclerView: Int = R.id.toCurrencyRecyclerView

    fun checkToCurrencies(vararg currencies: String) = currencies.forEachIndexed { i, currency ->
        onView(
            RecyclerViewMatcher(
                position = i,
                targetViewId = R.id.currencyTextView,
                recyclerViewId = toCurrencyRecyclerView
            )
        ).check(matches(withText(currency)))
    }

    fun chooseTo(position: Int) = onView(
        RecyclerViewMatcher(position = position, recyclerViewId = toCurrencyRecyclerView)
    ).perform(click())

    fun checkChosenTo(position: Int) = onView(
        RecyclerViewMatcher(
            position = position,
            targetViewId = R.id.chosenImageView,
            recyclerViewId = toCurrencyRecyclerView
        )
    ).run {
        check(matches(DrawableMatcher(R.drawable.check)))
        check(matches(isDisplayed()))
    }

    fun checkNoMoreCurrencies() = RecyclerViewMatcher(
        position = 0,
        targetViewId = R.id.noMoreCurrenciesTextView,
        recyclerViewId = toCurrencyRecyclerView
    ).matches(withText("No more currencies"))

    fun clickSave() = onView(
        allOf(
            withId(R.id.saveButton),
            isAssignableFrom(CustomButton::class.java),
            withParent(withId(rootId))
        )
    ).perform(click())

    fun goToDashboard() = onView(
        allOf(
            withId(R.id.backImageButton),
            isAssignableFrom(ImageButton::class.java),
            withParent(withId(rootId)),
            DrawableMatcher(R.drawable.back_arrow)
        )
    ).perform(click())
}