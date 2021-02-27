package com.example.schedule.ui.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.* // ktlint-disable
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.* // ktlint-disable
import androidx.test.filters.MediumTest
import com.example.schedule.R
import com.example.schedule.util.TestMethods
import com.example.schedule.util.TestMethods.Companion.clickXY
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

    @Test
    fun closeAddScheduleView_viaButton_ExpectScheduleToBeInvisible() {
        launchFragmentInHiltContainer<HomeFragment> ()
        onView(withId(R.id.addEvent)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.close)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.entryEvent)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun closeAddScheduleView_viaHomeFragment_ExpectScheduleToBeInvisible() {
        launchFragmentInHiltContainer<HomeFragment> ()
        onView(withId(R.id.addEvent)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.entryEvent)).perform(clickXY(10, 10))
        Thread.sleep(1000)
        onView(withId(R.id.entryEvent)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun openKeyboardFromAddScheduleView_ExpectKeyboardToBeVisible() {
        launchFragmentInHiltContainer<HomeFragment> ()
        onView(withId(R.id.addEvent)).perform(click())
        assert(!TestMethods.isKeyboardShown())
        Thread.sleep(1000)
        onView(withId(R.id.titleText)).perform(click())
        assert(TestMethods.isKeyboardShown())
    }
}
