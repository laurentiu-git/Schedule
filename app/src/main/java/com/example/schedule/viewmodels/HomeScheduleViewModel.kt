package com.example.schedule.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.* //ktlint-disable
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.repository.ScheduleItemsRepository
import com.example.schedule.util.Resource
import kotlinx.coroutines.launch
import java.util.* //ktlint-disable

class HomeScheduleViewModel @ViewModelInject constructor(
    private val scheduleItemsItemsRepository: ScheduleItemsRepository
) : ViewModel() {

    val daySchedule: MutableLiveData<Resource<String>> = MutableLiveData()
    val schedule = MediatorLiveData<List<ScheduleInfo>>()

    val date: MutableLiveData<Date> = MutableLiveData()
    var someSchedule: LiveData<List<ScheduleInfo>> = MutableLiveData()

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
        val currentDate = scheduleItemsItemsRepository.getDate(amount)
        date.postValue(currentDate)
        return currentDate
    }

    /*  fun getDate(amount: Int, swipe: Boolean): Date {
          schedule(scheduleItemsItemsRepository.getDate(amount))
          return getDate(0)
      }
     */

    fun currentScheduleList() {
        someSchedule = Transformations.switchMap(
            date
        ) { dateHasChanged ->
            scheduleItemsItemsRepository.getSchedules(dateHasChanged)
        }
    }
}
