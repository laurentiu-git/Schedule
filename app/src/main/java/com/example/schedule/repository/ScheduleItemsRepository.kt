package com.example.schedule.repository

import androidx.lifecycle.MutableLiveData
import com.example.schedule.data.local.ScheduleDatabase
import com.example.schedule.data.models.ScheduleInfo
import java.util.* //ktlint-disable
import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor(
    val db: ScheduleDatabase,
    val cal: Calendar
) {
    private val liveDate: MutableLiveData<Date> = MutableLiveData()

    suspend fun getScheduleItems() = "Yolo"

    suspend fun updateAndReplace(schedule: ScheduleInfo) = db.getScheduleDao().updateAndReplace(schedule)

    suspend fun deleteSchedule(schedule: ScheduleInfo) = db.getScheduleDao().deleteResult(schedule)

    fun getSchedules(date: Date) = db.getScheduleDao().getSchedule(date)

    fun getDate(amount: Int): Date {
        cal.add(Calendar.DATE, amount)
        return cal.time
    }
}
