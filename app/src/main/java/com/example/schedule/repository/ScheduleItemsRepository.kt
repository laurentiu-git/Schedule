package com.example.schedule.repository

import com.example.schedule.data.local.ScheduleDatabase
import com.example.schedule.data.models.ScheduleInfo
import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor(
    val db: ScheduleDatabase
) {

    suspend fun getScheduleItems() = "Yolo"

    suspend fun updateAndReplace(schedule: ScheduleInfo) = db.getScheduleDao().updateAndReplace(schedule)

    suspend fun deleteSchedule(schedule: ScheduleInfo) = db.getScheduleDao().deleteResult(schedule)

    fun getSchedules() = db.getScheduleDao().getSchedule()
}
