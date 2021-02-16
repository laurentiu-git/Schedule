package com.example.schedule.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.repository.ScheduleItemsRepository
import com.example.schedule.util.Resource
import kotlinx.coroutines.launch
import java.util.*

class HomeScheduleViewModel @ViewModelInject constructor(
    private val scheduleItemsItemsRepository: ScheduleItemsRepository
) : ViewModel() {

    val daySchedule: MutableLiveData<Resource<String>> = MutableLiveData()
    val schedule = MediatorLiveData<List<ScheduleInfo>>()

    init {
        getDay()
    }

    private fun getDay() = viewModelScope.launch {
        daySchedule.postValue(Resource.Loading())
        val response = scheduleItemsItemsRepository.getScheduleItems()
        daySchedule.postValue(scheduleItemsResponse(response))
    }

    private fun scheduleItemsResponse(response: String): Resource<String>? {
        if (response == "Yolo" || response == "30-01-2021") {
            return Resource.Success(response)
        }

        return Resource.Error("some error")
    }

    fun updateAndReplace(schedule: ScheduleInfo) = viewModelScope.launch {
        scheduleItemsItemsRepository.updateAndReplace(schedule)
    }

    fun schedule(date: Date) {
        schedule.addSource(scheduleItemsItemsRepository.getSchedules(date)) {
            schedule.postValue(it)
        }
    }

    fun deleteSchedule(schedule: ScheduleInfo) = viewModelScope.launch {
        scheduleItemsItemsRepository.deleteSchedule(schedule)
    }

    fun getDate(amount: Int): Date {
      //  schedule(scheduleItemsItemsRepository.getDate(amount))
        return scheduleItemsItemsRepository.getDate(amount)
    }

    fun getDate(amount: Int, swipe: Boolean): Date {
        schedule(scheduleItemsItemsRepository.getDate(amount))
        return getDate(0)
    }
}
