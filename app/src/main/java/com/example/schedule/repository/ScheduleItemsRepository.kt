package com.example.schedule.repository

import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor() {

    suspend fun getScheduleItems() = "today"

}