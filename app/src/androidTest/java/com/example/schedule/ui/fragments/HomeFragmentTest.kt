package com.example.schedule.ui.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.* // ktlint-disable
import androidx.test.filters.MediumTest
import com.example.schedule.R
import com.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun clickAddScheduleButton_ExpectedScheduleToBeVisible() {
        launchFragmentInHiltContainer<HomeFragment> ()
        onView(withId(R.id.addEvent)).perform(click())
        Thread.sleep(100)
        onView(withId(R.id.entryEvent)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}
