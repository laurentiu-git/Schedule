package com.example.schedule.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.FragmentHomeBinding
import com.example.schedule.ui.transitions.AddEventTransition
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}
