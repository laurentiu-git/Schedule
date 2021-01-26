package com.example.schedule.ui.transitions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.schedule.R

class EntryEvent : FrameLayout {

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
    }
}
