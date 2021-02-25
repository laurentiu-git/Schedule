package com.example.schedule.util

import com.example.schedule.data.models.ScheduleInfo
import java.util.* // ktlint-disable

class TestConstants {
    companion object {
        val date = Calendar.getInstance().time
        val schedule = ScheduleInfo(null, date, "", "", "", "", "")
        val scheduleList = listOf(schedule, schedule, schedule)
    }
}
