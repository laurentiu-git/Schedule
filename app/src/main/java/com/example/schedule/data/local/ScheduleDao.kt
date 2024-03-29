package com.example.schedule.data.local

import androidx.lifecycle.LiveData
import androidx.room.*  //ktlint-disable
import com.example.schedule.data.models.ScheduleInfo
import java.util.* //ktlint-disable

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAndReplace(result: ScheduleInfo)

    @Query("SELECT * FROM schedules WHERE date-:date  = 0")
    fun getSchedule(date: Date): LiveData<List<ScheduleInfo>>

    @Delete
    suspend fun deleteResult(result: ScheduleInfo)
}
