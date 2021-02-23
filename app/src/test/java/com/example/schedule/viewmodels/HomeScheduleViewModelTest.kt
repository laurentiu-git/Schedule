package com.example.schedule.viewmodels

import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.repository.FakeScheduleItemsRepository
import org.junit.Before
import org.junit.Test

class HomeScheduleViewModelTest {
    private lateinit var viewModel: HomeScheduleViewModel

    @Before
    fun setup() {
        viewModel = HomeScheduleViewModel(FakeScheduleItemsRepository())
    }

    @Test
    fun `inserting item`() {
        viewModel.updateAndReplace(ScheduleInfo(null, viewModel.getDate(0), "", "", "", "", ""))
    }
}
