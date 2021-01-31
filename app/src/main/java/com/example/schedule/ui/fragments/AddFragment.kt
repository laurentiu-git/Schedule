package com.example.schedule.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.schedule.R
import com.example.schedule.databinding.FragmentAddBinding

class AddFragment : Fragment(R.layout.fragment_add) {

    private var fragmentBinding: FragmentAddBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddBinding.bind(view)
        fragmentBinding = binding
        binding.textView.text = getString(R.string.hello)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}
