package com.example.schedule.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.FragmentHomeBinding
import com.example.schedule.ui.transitions.AddEventTransition
import com.example.schedule.ui.transitions.EntryEventListener
import com.example.schedule.util.Resource
import com.example.schedule.viewmodels.HomeScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.* //ktlint-disable

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), DatePickerDialog.OnDateSetListener {

    private var fragmentBinding: FragmentHomeBinding? = null
    lateinit var binding: FragmentHomeBinding
    lateinit var homeScheduleViewModel : HomeScheduleViewModel

    var day = 0
    var month = 0
    var year = 0
    private var previousDay = -1
    private var nextDay = 1
    private val cal = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        fragmentBinding = binding

        homeScheduleViewModel = ViewModelProvider(requireActivity()).get(HomeScheduleViewModel::class.java)


        binding.addEvent.setOnClickListener {
            val animation = AddEventTransition(binding.addEvent, binding.entryEvent)
            animation.openCalendar()
        }

        binding.entryEvent.setAddEventListener(
            object : EntryEventListener {
                override fun onCloseClicked() {
                    val animation = AddEventTransition(binding.addEvent, binding.entryEvent)
                    animation.closeCalendar()
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
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.dayId.setOnClickListener {
            pickDate(it)
        }

        binding.arrowLeft.setOnClickListener {
         //   cal.add(Calendar.DATE, previousDay)
       //     var dateFromat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
          //  binding.dayId.text = dateFromat.format(cal.time)
        }

        homeScheduleViewModel.day.observe(viewLifecycleOwner) {
            response -> when (response) {
            is Resource.Success -> {
                binding.dayId.text = response.data
             }
            }
        }

        binding.arrowRight.setOnClickListener {
            cal.add(Calendar.DATE, nextDay)
            var dateFromat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            binding.dayId.text = dateFromat.format(cal.time)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    private fun pickDate(view: View) {
        getDateCalendar()
        DatePickerDialog(view.context, this, year, month, day).show()
    }

    private fun getDateCalendar() {
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year, month, dayOfMonth)
        binding.dayId.text = "$dayOfMonth-${month + 1}-$year"
    }
}
