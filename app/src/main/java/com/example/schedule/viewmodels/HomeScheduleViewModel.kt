package com.example.schedule.viewmodels

import androidx.lifecycle.* //ktlint-disable
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.util.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.* //ktlint-disable
import javax.inject.Inject

@HiltViewModel
class HomeScheduleViewModel @Inject constructor(
    private val scheduleItemsItemsRepository: ScheduleRepository
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
