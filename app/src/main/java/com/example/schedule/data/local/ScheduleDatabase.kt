package com.example.schedule.data.local

import android.content.Context
import androidx.room.* //ktlint-disable
import com.example.schedule.data.models.ScheduleInfo

@Database(
    entities = [ScheduleInfo::class],
    version = 1
)

abstract class ScheduleDatabase : RoomDatabase() {

    abstract fun getScheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var instance: ScheduleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                ScheduleDatabase::class.java,
                "scheduledb.db"
            ).build()
    }
}
