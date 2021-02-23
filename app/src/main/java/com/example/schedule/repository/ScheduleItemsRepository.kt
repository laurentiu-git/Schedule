package com.example.schedule.repository

import androidx.lifecycle.LiveData
import com.example.schedule.data.local.ScheduleDatabase
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.util.ScheduleRepository
import java.text.SimpleDateFormat //ktlint-disable
import java.util.* //ktlint-disable
import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor(
    val db: ScheduleDatabase,
    val cal: Calendar
) : ScheduleRepository {
    override suspend fun updateAndReplace(schedule: ScheduleInfo) {
        db.getScheduleDao().updateAndReplace(schedule)
    }

    override suspend fun deleteSchedule(schedule: ScheduleInfo) {
        db.getScheduleDao().deleteResult(schedule)
    }

    override fun getSchedules(date: Date): LiveData<List<ScheduleInfo>> {
        return db.getScheduleDao().getSchedule(date)
    }

    override fun getDate(amount: Int): Date {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(dateFormat.format(cal.time))
    }
}
