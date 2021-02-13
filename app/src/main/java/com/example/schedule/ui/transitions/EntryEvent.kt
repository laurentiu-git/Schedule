package com.example.schedule.ui.transitions

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.schedule.BuildConfig
import com.example.schedule.R
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.databinding.AddEventFragmentBinding
import com.example.schedule.ui.adapters.TimeAdapter
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EntryEvent : FrameLayout {

    private var entryEvent: EntryEventListener? = null
    @Inject
    lateinit var timeAdapter: TimeAdapter
    @Inject
    lateinit var minutesAdapter: TimeAdapter

    private val snapHelper = LinearSnapHelper()

    private lateinit var location: String

    private var view: View? = null

    private var fragmentBinding: AddEventFragmentBinding? = null

    private val list = mutableListOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24")
    private val listMinutes = mutableListOf(
        "00",
        "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
        "54", "55", "56", "57", "58", "59"
    )

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.add_event_fragment, this)
        val binding = AddEventFragmentBinding.bind(view)
        fragmentBinding = binding

        setOnClickListener {
            entryEvent?.onCloseClicked()
            hideKeyboardFrom(context, it)
        }

        binding.close.setOnClickListener {
            entryEvent?.onCloseClicked()
            hideKeyboardFrom(context, it)
        }

        binding.addLayout.setOnClickListener {
            hideKeyboardFrom(context, it)
        }

        timeAdapter.differ.submitList(list)
        binding.hourList.apply {
            adapter = timeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.hourList.scrollToPosition(Integer.MAX_VALUE / 2)
        snapHelper.attachToRecyclerView(binding.hourList)

        minutesAdapter.differ.submitList(listMinutes)
        binding.minutesList.apply {
            adapter = minutesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.minutesList.scrollToPosition(Integer.MAX_VALUE / 2)
        LinearSnapHelper().attachToRecyclerView(binding.minutesList)

        binding.hourTimeForEnd.apply {
            adapter = timeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.hourTimeForEnd.scrollToPosition(Integer.MAX_VALUE / 2)
        LinearSnapHelper().attachToRecyclerView(binding.hourTimeForEnd)

        binding.minutesTimeForEnd.apply {
            adapter = minutesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.minutesTimeForEnd.scrollToPosition(Integer.MAX_VALUE / 2)
        LinearSnapHelper().attachToRecyclerView(binding.minutesTimeForEnd)

        binding.addBtn.setOnClickListener {
            val hour = timeAdapter.getTime(snapHelper.getSnapPosition(binding.hourList))
            val hourEnd = timeAdapter.getTime(snapHelper.getSnapPosition(binding.hourTimeForEnd))
            val minutes = minutesAdapter.getTimeMinutes(snapHelper.getSnapPosition(binding.minutesList))
            val minutesEnd = minutesAdapter.getTimeMinutes(snapHelper.getSnapPosition(binding.minutesTimeForEnd))
            val schedule = ScheduleInfo(
                null,
                "",
                "",
                "",
                "$hour:$minutes",
                "$hourEnd:$minutesEnd",
                binding.titleText.text.toString(),
                binding.description.text.toString(),
                ""
            )
            entryEvent?.addSchedule(schedule)
            entryEvent?.onCloseClicked()
        }

        //addPlaceListener()
        binding.startTime.setOnClickListener {
            entryEvent?.searchLocation()
        }
    }

    fun setAddEventListener(entryEventListener: EntryEventListener) {
        this.entryEvent = entryEventListener
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        fragmentBinding = null
    }
}

interface EntryEventListener {
    fun onCloseClicked()
    fun addSchedule(schedule: ScheduleInfo)
    fun searchLocation()
}
