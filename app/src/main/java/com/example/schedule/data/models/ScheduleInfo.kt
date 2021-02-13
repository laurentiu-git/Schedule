package com.example.schedule.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedules"
)
data class ScheduleInfo(
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int? = null,
    var year: String,
    var month: String,
    var day: String,
    var startTime: String,
    var endTime: String,
    var taskName: String,
    var description: String,
    var location: String,
)
