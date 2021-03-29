package com.example.schedule.data.local

import androidx.room.* //ktlint-disable
import com.example.schedule.data.converters.Converters
import com.example.schedule.data.models.ScheduleInfo

@Database(
    entities = [ScheduleInfo::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract fun getScheduleDao(): ScheduleDao
}
