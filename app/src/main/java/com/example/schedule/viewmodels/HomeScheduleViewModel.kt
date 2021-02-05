package com.example.schedule.viewmodels

import android.location.Address
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.repository.ScheduleItemsRepository
import com.example.schedule.util.Resource
import kotlinx.coroutines.launch

class HomeScheduleViewModel @ViewModelInject constructor(
    private val scheduleItemsItemsRepository: ScheduleItemsRepository
) : ViewModel() {

    val daySchedule: MutableLiveData<Resource<String>> = MutableLiveData()
    val locationList: MutableLiveData<List<Address>> = MutableLiveData()
    var schedule = MediatorLiveData<List<ScheduleInfo>>()

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

    fun getSchedule(day: String) = scheduleItemsItemsRepository.getSchedules(day)

    fun schedule(day: String) {
        schedule.addSource(scheduleItemsItemsRepository.getSchedules(day)) {
            schedule.value = it
        }
    }

    fun getLocation(location: String) {
        locationList.postValue(scheduleItemsItemsRepository.getLocation(location))
    }
}
