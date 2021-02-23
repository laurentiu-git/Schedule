package com.example.schedule.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.util.ScheduleRepository
import java.util.*

class FakeScheduleItemsRepository: ScheduleRepository {

    private val scheduleItems = mutableListOf<ScheduleInfo>()

    private  val observableScheduleItems = MutableLiveData<List<ScheduleInfo>>(scheduleItems)

    private fun refreshLiveData() {
        observableScheduleItems.postValue(scheduleItems)
    }

    override suspend fun updateAndReplace(schedule: ScheduleInfo) {
        scheduleItems.add(schedule)
        refreshLiveData()
    }

    override suspend fun deleteSchedule(schedule: ScheduleInfo) {
        scheduleItems.remove(schedule)
        refreshLiveData()
    }

    override fun getSchedules(date: Date): LiveData<List<ScheduleInfo>> {
         return observableScheduleItems
    }

    override fun getDate(amount: Int): Date {
        return Calendar.getInstance().time
    }
}