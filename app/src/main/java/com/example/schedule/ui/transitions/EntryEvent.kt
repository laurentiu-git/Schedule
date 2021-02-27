package com.example.schedule.ui.transitions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.schedule.R
import com.example.schedule.data.models.ScheduleInfo
import com.example.schedule.databinding.AddEventFragmentBinding
import com.example.schedule.ui.adapters.TimeAdapter
import com.example.schedule.ui.fragments.HomeFragment
import com.example.schedule.util.Constants
import com.example.schedule.util.EntryEventListener
import com.example.schedule.util.LocationListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.* //ktlint-disable
import javax.inject.Inject

@AndroidEntryPoint
class EntryEvent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var entryEvent: EntryEventListener? = null
    @Inject
    lateinit var timeAdapter: TimeAdapter
    @Inject
    lateinit var minutesAdapter: TimeAdapter

    private val snapHelper = LinearSnapHelper()

    private var fragmentBinding: AddEventFragmentBinding? = null

    init {
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

        timeAdapter.differ.submitList(Constants.hourList)
        binding.hourList.apply {
            adapter = timeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.hourList.scrollToPosition(Integer.MAX_VALUE / 2)
        snapHelper.attachToRecyclerView(binding.hourList)

        minutesAdapter.differ.submitList(Constants.minutesList)
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
                Calendar.getInstance().time,
                "$hour:$minutes",
                "$hourEnd:$minutesEnd",
                binding.titleText.text.toString(),
                binding.firstTask.text.toString(),
                ""
            )
            entryEvent?.addSchedule(schedule)
            hideKeyboardFrom(context, it)
            entryEvent?.onCloseClicked()
        }

        binding.location.setOnClickListener {
            entryEvent?.searchLocation()
        }

        val fragment = getForegroundFragment()

        val homeFragment = fragment?.let {
            it as HomeFragment
        }

        homeFragment?.setLocation(
            object : LocationListener {
                override fun setLocation(location: String) {
                    binding.location.text = location
                }
            }
        )
    }

    fun setAddEventListener(entryEventListener: EntryEventListener) {
        this.entryEvent = entryEventListener
    }
    private fun getForegroundFragment(): Fragment? {
        val navHostFragment: Fragment? = getActivity()?.supportFragmentManager?.findFragmentById(R.id.navHostFragment)
        return if (navHostFragment == null) null else navHostFragment.childFragmentManager.fragments[0]
    }
    private fun getActivity(): FragmentActivity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is FragmentActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
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
