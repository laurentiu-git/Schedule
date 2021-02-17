package com.example.schedule.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.* //ktlint-disable
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.repository.ScheduleItemsRepository
import kotlinx.coroutines.launch
import java.util.* //ktlint-disable

class HomeScheduleViewModel @ViewModelInject constructor(
    private val scheduleItemsItemsRepository: ScheduleItemsRepository
) : ViewModel() {

    val date: MutableLiveData<Date> = MutableLiveData()
    var scheduleList: LiveData<List<ScheduleInfo>> = MutableLiveData()

    fun updateAndReplace(schedule: ScheduleInfo) = viewModelScope.launch {
        scheduleItemsItemsRepository.updateAndReplace(schedule)
    }

    fun deleteSchedule(schedule: ScheduleInfo) = viewModelScope.launch {
        scheduleItemsItemsRepository.deleteSchedule(schedule)
    }

    fun getDate(amount: Int): Date {
        val currentDate = scheduleItemsItemsRepository.getDate(amount)
        date.postValue(currentDate)
        return currentDate
    }

    fun currentScheduleList() {
        scheduleList = Transformations.switchMap(
            date
        ) { dateHasChanged ->
            scheduleItemsItemsRepository.getSchedules(dateHasChanged)
        }
    }
}
