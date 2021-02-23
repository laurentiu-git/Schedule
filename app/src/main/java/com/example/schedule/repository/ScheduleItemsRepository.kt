package com.example.schedule.repository

import androidx.lifecycle.LiveData
import com.example.schedule.data.local.ScheduleDatabase
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.util.ScheduleItemsRepository
import java.text.SimpleDateFormat //ktlint-disable
import java.util.* //ktlint-disable
import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor(
    val db: ScheduleDatabase,
    val cal: Calendar
) : ScheduleItemsRepository {
    override suspend fun updateAndReplace(schedule: ScheduleInfo): Long {
        return db.getScheduleDao().updateAndReplace(schedule)
    }

    override suspend fun deleteSchedule(schedule: ScheduleInfo) {
        db.getScheduleDao().deleteResult(schedule)
    }

    override fun getSchedules(date: Date): LiveData<List<ScheduleInfo>> {
        return db.getScheduleDao().getSchedule(date)
    }

    override fun getDate(amount: Int): Date {
        cal.add(Calendar.DATE, amount)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.parse(dateFormat.format(cal.time))
    }
}
