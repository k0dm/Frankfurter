package com.example.frankfurter

import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
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

class SubscriptionPage {

    private val rootId: Int = R.id.subscriptionLayout
    private val rootLayout = onView(
        allOf(
            withId(rootId),
            isAssignableFrom(LinearLayout::class.java)
        )
    )

    fun checkVisible() {
        rootLayout.check(matches(isDisplayed()))
        onView(
            allOf(
                withId(R.id.subscriptionTitleTextView),
                isAssignableFrom(TextView::class.java),
                withText("Go Premium"),
                withParent(withId(rootId))
            )
        ).check(matches(isDisplayed()))
        onView(
            allOf(
                withId(R.id.subscriptionInfoTextView),
                isAssignableFrom(TextView::class.java),
                withText("To add new currency pair buy a premium"),
                withParent(withId(rootId))
            )
        ).check(matches(isDisplayed()))
    }

    fun checkNotVisible() = rootLayout.check(doesNotExist())

    fun clickBuyPremium() = onView(
        allOf(
            withId(R.id.buyButton),
            isAssignableFrom(Button::class.java),
            withText("Buy"),
            withParent(withId(rootId))
        )
    ).perform(click())

    fun comeback() = onView(
        allOf(
            withId(R.id.goToSettingsImageButton),
            isAssignableFrom(ImageButton::class.java),
            withParent(withId(rootId)),
            DrawableMatcher(R.drawable.back_arrow)
        )
    ).perform(click())
}