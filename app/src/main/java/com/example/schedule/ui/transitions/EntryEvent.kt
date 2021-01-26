package com.example.schedule.ui.transitions

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.example.schedule.R

class EntryEvent(context: Context) : FrameLayout(context) {

    init {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.fragment_home, this)
    }
}