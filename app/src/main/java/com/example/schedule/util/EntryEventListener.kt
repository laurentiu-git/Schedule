package com.example.schedule.util

import com.example.schedule.data.models.ScheduleInfo

interface EntryEventListener {
    fun onCloseClicked()
    fun addSchedule(schedule: ScheduleInfo)
    fun searchLocation()
}
