package com.example.schedule.ui.transitions

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.* //ktlint-disable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.schedule.R
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.ui.adapters.TimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.* //ktlint-disable
import javax.inject.Inject

@AndroidEntryPoint
class EntryEvent : FrameLayout {

    private var entryEvent: EntryEventListener? = null
    @Inject
    lateinit var timeAdapter: TimeAdapter
    @Inject
    lateinit var minutesAdapter: TimeAdapter

    private val snapHelper = LinearSnapHelper()

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
        View.inflate(context, R.layout.add_event_fragment, this)

        setOnClickListener {
            entryEvent?.onCloseClicked()
            hideKeyboardFrom(context, it)
        }

        val closeElement = findViewById<TextView>(R.id.close)
        closeElement.setOnClickListener {
            entryEvent?.onCloseClicked()
            hideKeyboardFrom(context, it)
        }

        val addLayout = findViewById<ConstraintLayout>(R.id.addLayout)
        addLayout.setOnClickListener {
            hideKeyboardFrom(context, it)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.hourList)
        val recyclerViewMinutes = findViewById<RecyclerView>(R.id.minutesList)

        timeAdapter.differ.submitList(list)

        recyclerView.apply {
            adapter = timeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        recyclerView.scrollToPosition(Integer.MAX_VALUE / 2)
        snapHelper.attachToRecyclerView(recyclerView)

        minutesAdapter.differ.submitList(listMinutes)

        recyclerViewMinutes.apply {
            adapter = minutesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        recyclerViewMinutes.scrollToPosition(Integer.MAX_VALUE / 2)
        LinearSnapHelper().attachToRecyclerView(recyclerViewMinutes)

        val title = findViewById<EditText>(R.id.titleText)
        val description = findViewById<EditText>(R.id.description)

        val addBtn = findViewById<Button>(R.id.button)
        addBtn.setOnClickListener {
            val hour = timeAdapter.getTime(snapHelper.getSnapPosition(recyclerView))
            val minutes = minutesAdapter.getTimeMinutes(snapHelper.getSnapPosition(recyclerViewMinutes))
            val schedule = ScheduleInfo(
                null,
                "",
                "",
                "",
                "$hour:$minutes",
                title.text.toString(),
                description.text.toString()
            )
            entryEvent?.addSchedule(schedule)
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
}

interface EntryEventListener {
    fun onCloseClicked()
    fun addSchedule(schedule: ScheduleInfo)
}
