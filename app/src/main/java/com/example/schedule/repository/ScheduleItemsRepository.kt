package com.example.schedule.repository

import android.location.Geocoder
import com.example.schedule.data.local.ScheduleDatabase
import com.example.schedule.data.models.ScheduleInfo
import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor(
    val db: ScheduleDatabase,
    val geocoder: Geocoder
) {

    suspend fun getScheduleItems() = "Yolo"

    suspend fun updateAndReplace(schedule: ScheduleInfo) = db.getScheduleDao().updateAndReplace(schedule)

    suspend fun deleteSchedule(schedule: ScheduleInfo) = db.getScheduleDao().deleteResult(schedule)

    fun getSchedules(day: String) = db.getScheduleDao().getSchedule(day)

    fun getLocation(location: String) = geocoder.getFromLocationName(location, 3)
}
