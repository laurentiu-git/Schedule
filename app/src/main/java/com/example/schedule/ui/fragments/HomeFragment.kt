package com.example.schedule.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.BuildConfig
import com.example.schedule.R
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.databinding.FragmentHomeBinding
import com.example.schedule.ui.adapters.ScheduleAdapter
import com.example.schedule.ui.transitions.AddEventTransition
import com.example.schedule.util.Constants.Companion.AUTOCOMPLETE_REQUEST_CODE
import com.example.schedule.util.EntryEventListener
import com.example.schedule.util.LocationListener
import com.example.schedule.viewmodels.HomeScheduleViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.* //ktlint-disable
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), DatePickerDialog.OnDateSetListener {
    private var fragmentBinding: FragmentHomeBinding? = null

    lateinit var homeScheduleViewModel: HomeScheduleViewModel
    @Inject
    lateinit var scheduleAdapter: ScheduleAdapter

    private var location: String = ""
    private var locationListener: LocationListener? = null

    @Inject
    lateinit var cal: Calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        fragmentBinding = binding

        homeScheduleViewModel = ViewModelProvider(requireActivity()).get(HomeScheduleViewModel::class.java)

        binding.homeRecyclerView.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

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
                    val scheduleInfo = ScheduleInfo(
                        null,
                        homeScheduleViewModel.getDate(0),
                        schedule.startTime,
                        schedule.endTime,
                        schedule.taskName,
                        schedule.description,
                        location
                    )
                    homeScheduleViewModel.updateAndReplace(scheduleInfo)
                }

                override fun searchLocation() {
                    val fields = listOf(Place.Field.ID, Place.Field.NAME)
                    // Start the autocomplete intent.
                    val intent = context?.let {
                        if (!Places.isInitialized()) {
                            Places.initialize(it, BuildConfig.API_PLACES)
                        }
                        Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(it)
                    }
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
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
            homeScheduleViewModel.getDate(previousDay)
        }

        binding.arrowRight.setOnClickListener {
            val nextDay = 1
            homeScheduleViewModel.getDate(nextDay)
        }

        homeScheduleViewModel.getDate(0)
        homeScheduleViewModel.date.observe(
            viewLifecycleOwner,
            {
                result ->
                val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
                binding.dayId.text = dateFormat.format(result)
            }
        )

        homeScheduleViewModel.currentScheduleList()
        homeScheduleViewModel.scheduleList.observe(
            viewLifecycleOwner,
            { result ->
                scheduleAdapter.differ.submitList(result)
            }
        )

      /*  binding.homeFrag.setOnTouchListener(
            object : OnSwipeTouchListener(view.context) {
                override fun onSwipeLeft() {
                    val nextDay = 1
                    binding.dayId.text = getCurrentDate(homeScheduleViewModel.getDate(nextDay))
                }

                override fun onSwipeRight() {
                    val previousDay = -1
                    binding.dayId.text = getCurrentDate(homeScheduleViewModel.getDate(previousDay))
                }
            }
        )
       */
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val schedule = scheduleAdapter.differ.currentList[position]
                homeScheduleViewModel.deleteSchedule(schedule)
                Snackbar.make(view, "Schedule Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        homeScheduleViewModel.updateAndReplace(schedule)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.homeRecyclerView)
        }
    }

    private fun pickDate(view: View) {
        DatePickerDialog(
            view.context,
            this,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cal.set(year, month, dayOfMonth)
        homeScheduleViewModel.getDate(0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i("TAG", "Place: ${place.name}, ${place.id}")
                        place.name?.let {
                            location = it
                        } ?: ""
                        locationListener?.setLocation(location)
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        status.statusMessage?.let { it1 -> Log.i("TAG", it1) }
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding?.homeRecyclerView?.adapter = null
        this.locationListener = null
        fragmentBinding = null
    }
    fun setLocation(locationListener: LocationListener) {
        this.locationListener = locationListener
    }
}
