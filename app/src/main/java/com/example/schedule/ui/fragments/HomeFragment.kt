package com.example.schedule.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import com.example.schedule.util.EntryEventListener
import com.example.schedule.util.LocationListener
import com.example.schedule.util.OnSwipeTouchListener
import com.example.schedule.viewmodels.HomeScheduleViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable
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

    @SuppressLint("ClickableViewAccessibility")
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

        scheduleAdapter.setOnItemClickListener {
            bottomNav?.isVisible = false
            if (it == -1) {
                context?.let { it1 -> hideKeyboardFrom(it1, view) }
            }
        }

        scheduleAdapter.setOnAddClickListener {
            homeScheduleViewModel.updateAndReplace(it)
        }

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
                        location,
                        schedule.taskList
                    )
                    homeScheduleViewModel.updateAndReplace(scheduleInfo)
                }

                val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        // There are no request codes
                        val data: Intent? = result.data
                        val place = data?.let { Autocomplete.getPlaceFromIntent(it) }
                        place?.name?.let {
                            location = it
                        }
                        locationListener?.setLocation(location)
                    }
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
                    resultLauncher.launch(intent)
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
            { result ->
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

        binding.homeFrag.setOnTouchListener(
            object : OnSwipeTouchListener(view.context) {
                override fun onSwipeLeft() {
                    val nextDay = 1
                    homeScheduleViewModel.getDate(nextDay)
                }

                override fun onSwipeRight() {
                    val previousDay = -1
                    homeScheduleViewModel.getDate(previousDay)
                }
            }
        )

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

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding?.homeRecyclerView?.adapter = null
        this.locationListener = null
        fragmentBinding = null
    }

    fun setLocation(locationListener: LocationListener) {
        this.locationListener = locationListener
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
