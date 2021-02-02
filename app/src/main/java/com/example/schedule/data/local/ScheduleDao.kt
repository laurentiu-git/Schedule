package com.example.schedule.data.local

import androidx.lifecycle.LiveData
import androidx.room.*  //ktlint-disable
import com.example.schedule.data.models.ScheduleInfo

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAndReplace(result: ScheduleInfo): Long

    @Query("SELECT * FROM schedules WHERE day=:day")
    fun getSchedule(day: String): LiveData<List<ScheduleInfo>>

    @Delete
    suspend fun deleteResult(result: ScheduleInfo)
}
