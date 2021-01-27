package com.example.schedule.ui.transitions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.example.schedule.R
import java.util.*

class EntryEvent : FrameLayout {

    private var entryEvent: EntryEventListener? = null


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

        val cancelBtn = findViewById<Button>(R.id.cancelBtn)

        cancelBtn.setOnClickListener {
            entryEvent?.onCloseClicked()
        }

    }

    fun setAddEventListener(entryEventListener: EntryEventListener) {
        this.entryEvent = entryEventListener
    }
}


interface EntryEventListener {

    fun onCloseClicked()
}