package com.example.schedule.util

import androidx.lifecycle.LiveData
import com.example.schedule.data.models.ScheduleInfo
import java.util.* //ktlint-disable

interface ScheduleRepository {
    suspend fun updateAndReplace(schedule: ScheduleInfo)

    suspend fun deleteSchedule(schedule: ScheduleInfo)

    fun getSchedules(date: Date): LiveData<List<ScheduleInfo>>

    fun getDate(amount: Int): Date
}
