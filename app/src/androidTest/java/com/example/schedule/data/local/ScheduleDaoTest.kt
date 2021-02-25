package com.example.schedule.data.local

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.SmallTest
import com.example.schedule.R
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.getOrAwaitValue
import com.example.schedule.ui.fragments.HomeFragment
import com.example.schedule.util.TestConstants
import com.google.common.truth.Truth.assertThat
import com.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.* // ktlint-disable
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ScheduleDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var db: ScheduleDatabase
    lateinit var dao: ScheduleDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = db.getScheduleDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertScheduleItem() = runBlockingTest {
        val date = Calendar.getInstance().time
        val schedule = ScheduleInfo(1, date, "", "", "", "", "")
        dao.updateAndReplace(schedule)

        val allScheduleItems = dao.getSchedule(date).getOrAwaitValue()

        assertThat(allScheduleItems).contains(schedule)
    }

    @Test
    fun deleteScheduleItem() = runBlockingTest {
        val date = Calendar.getInstance().time
        val schedule = ScheduleInfo(1, date, "", "", "", "", "")

        dao.updateAndReplace(schedule)
        dao.deleteResult(schedule)
        val allScheduleItems = dao.getSchedule(date).getOrAwaitValue()

        assertThat(allScheduleItems).isEmpty()
    }

    @Test
    fun checkObservableItems_expectedSameElementsSize()= runBlockingTest {
        for(schedule in TestConstants.scheduleList) {
           dao.updateAndReplace(schedule)
       }

        val allScheduleInfo = dao.getSchedule(TestConstants.date).getOrAwaitValue()

        assertThat(allScheduleInfo.size).isEqualTo(TestConstants.scheduleList.size)
    }

    @Test
    fun launchFragmentInHiltContainer() {
        launchFragmentInHiltContainer<HomeFragment>()
        // launchFragmentInContainer<HomeFragment>()
    }
}
