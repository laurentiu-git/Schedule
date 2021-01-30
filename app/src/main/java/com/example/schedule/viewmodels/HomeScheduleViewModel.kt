package com.example.schedule.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.repository.ScheduleItemsRepository
import com.example.schedule.util.Resource
import kotlinx.coroutines.launch

class HomeScheduleViewModel @ViewModelInject constructor(
        private val scheduleItemsItemsRepository: ScheduleItemsRepository
) : ViewModel() {

    val day: MutableLiveData<Resource<String>> = MutableLiveData()

    init{
       getDay()
    }

    private fun getDay() = viewModelScope.launch {
        day.postValue(Resource.Loading())
        val response = scheduleItemsItemsRepository.getScheduleItems()
        day.postValue(scheduleItemsResponse(response))
    }

    private fun scheduleItemsResponse(response: String): Resource<String>? {
        if (response == "today") {
            return Resource.Success(response)
        }

        return Resource.Error("some error")
    }


}