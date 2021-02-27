package com.example.schedule.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.* //ktlint-disable

@Entity(
    tableName = "schedules"
)
data class ScheduleInfo(
        @PrimaryKey(autoGenerate = true)
    var primaryKey: Int? = null,
        var date: Date,
        var startTime: String,
        var endTime: String,
        var taskName: String,
        var firstTask: String,
        var location: String,
)
