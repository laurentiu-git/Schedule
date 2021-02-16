package com.example.schedule.repository

import com.example.schedule.data.local.ScheduleDatabase
import com.example.schedule.data.models.ScheduleInfo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor(
        val db: ScheduleDatabase,
        val cal: Calendar
) {

    suspend fun getScheduleItems() = "Yolo"

    suspend fun updateAndReplace(schedule: ScheduleInfo) = db.getScheduleDao().updateAndReplace(schedule)

    suspend fun deleteSchedule(schedule: ScheduleInfo) = db.getScheduleDao().deleteResult(schedule)

     fun getSchedules(date: Date) = db.getScheduleDao().getSchedule(date)

     fun getDate(amount: Int): Date {
         cal.add(Calendar.DATE, amount)
         val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
         return dateFormat.parse(dateFormat.format(cal.time))
     }
}
