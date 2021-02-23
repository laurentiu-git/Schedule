package com.example.schedule.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.schedule.MainCoroutineRule
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.repository.FakeScheduleItemsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeScheduleViewModelTest {
    private lateinit var viewModel: HomeScheduleViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = HomeScheduleViewModel(FakeScheduleItemsRepository())
    }

    @Test
    fun `inserting item`() {
        viewModel.updateAndReplace(ScheduleInfo(null, viewModel.getDate(0), "", "", "", "", ""))
    }

    @Test
    fun `delete item`() {
        val scheduleInfo = ScheduleInfo(null, viewModel.getDate(0), "", "", "", "", "")
        viewModel.updateAndReplace(scheduleInfo)
        viewModel.deleteSchedule(scheduleInfo)
    }

    @Test
    fun `get schedule list`() {
        val scheduleInfo = ScheduleInfo(null, viewModel.getDate(0), "", "", "", "", "")
        viewModel.updateAndReplace(scheduleInfo)
    }
}
