package com.example.schedule.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.R
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.databinding.FragmentHomeBinding
import com.example.schedule.ui.adapters.ScheduleAdapter
import com.example.schedule.ui.transitions.AddEventTransition
import com.example.schedule.ui.transitions.EntryEventListener
import com.example.schedule.viewmodels.HomeScheduleViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.* //ktlint-disable
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), DatePickerDialog.OnDateSetListener {

    private var fragmentBinding: FragmentHomeBinding? = null
    lateinit var binding: FragmentHomeBinding
    lateinit var homeScheduleViewModel: HomeScheduleViewModel
    @Inject
    lateinit var scheduleAdapter: ScheduleAdapter

    private val cal = Calendar.getInstance()
    lateinit var currentDate: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fragmentBinding = binding

        homeScheduleViewModel = ViewModelProvider(requireActivity()).get(HomeScheduleViewModel::class.java)

        setupRecyclerView()
        var bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val simpleDateFormat = SimpleDateFormat("EEE, MMM d")
        currentDate = simpleDateFormat.format(Date())

        binding.addEvent.setOnClickListener {
            val animation = AddEventTransition(binding.addEvent, binding.entryEvent)
            animation.openCalendar()
            bottomNav?.isVisible = false
        }

        binding.entryEvent.setAddEventListener(
            object : EntryEventListener {
                override fun onCloseClicked() {
                    val animation = AddEventTransition(binding.addEvent, binding.entryEvent)
                    animation.closeCalendar()
                    bottomNav?.isVisible = true
                }

                override fun addSchedule(schedule: ScheduleInfo) {
                    val year = cal.get(Calendar.YEAR).toString()
                    val month = cal.get(Calendar.MONTH).toString()
                    val day = cal.get(Calendar.DAY_OF_MONTH).toString()
                    val scheduleInfo = ScheduleInfo(
                        null,
                        year,
                        month,
                        day,
                        schedule.hour,
                        schedule.taskName,
                        schedule.description
                    )
                    homeScheduleViewModel.updateAndReplace(scheduleInfo)
                }
            }
        )

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!binding.entryEvent.isVisible) {
                    if (!findNavController().navigateUp()) {
                        // nothing left in back stack we can finish the activity
                        requireActivity().finish()
                    }
                } else {
                    val animation = AddEventTransition(binding.addEvent, binding.entryEvent)
                    animation.closeCalendar()
                    bottomNav?.isVisible = true
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.dayId.setOnClickListener {
            pickDate(it)
        }

        binding.arrowLeft.setOnClickListener {
            val previousDay = -1
            binding.dayId.text = getDay(previousDay)
            homeScheduleViewModel.schedule(cal.get(Calendar.DAY_OF_MONTH).toString())
        }

        binding.arrowRight.setOnClickListener {
            val nextDay = 1
            binding.dayId.text = getDay(nextDay)
            homeScheduleViewModel.schedule(cal.get(Calendar.DAY_OF_MONTH).toString())
        }

        homeScheduleViewModel.day = cal.get(Calendar.DAY_OF_MONTH).toString()
        homeScheduleViewModel.schedule.observe(
            viewLifecycleOwner,
            {
                result ->
                scheduleAdapter.differ.submitList(result)
            }
        )
    }

    private fun setupRecyclerView() {
        binding.homeRecyclerView.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun pickDate(view: View) {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(view.context, this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year, month, dayOfMonth)
        binding.dayId.text = getCurrentDate(cal.time)
    }

    private fun getDay(position: Int): String {
        cal.add(Calendar.DATE, position)
        return if (currentDate != getCurrentDate(cal.time))
            getCurrentDate(cal.time)
        else
            "Today"
    }

    private fun getCurrentDate(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
        return dateFormat.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}
