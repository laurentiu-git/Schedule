package com.example.schedule.repository

import javax.inject.Inject

class ScheduleItemsRepository @Inject constructor() {

    suspend fun getScheduleItems() = "Yolo"

    suspend fun getCurrentDay() = "30-01-2021"
}
