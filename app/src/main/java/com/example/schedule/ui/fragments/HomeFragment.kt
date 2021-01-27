package com.example.schedule.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.schedule.R
import com.example.schedule.databinding.FragmentHomeBinding
import com.example.schedule.ui.transitions.AddEventTransition
import com.example.schedule.ui.transitions.EntryEvent
import com.example.schedule.ui.transitions.EntryEventListener

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var fragmentBinding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        binding.addEvent.setOnClickListener {
            val animation = AddEventTransition(binding.addEvent, binding.entryEvent)
            animation.openCalendar()
        }

        binding.entryEvent.setAddEventListener(object: EntryEventListener {
            override fun onCloseClicked() {
                val animation = AddEventTransition(binding.addEvent, binding.entryEvent)
                animation.closeCalendar()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}
