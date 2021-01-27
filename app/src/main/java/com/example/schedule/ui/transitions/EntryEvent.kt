package com.example.schedule.ui.transitions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.example.schedule.R

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

        setOnClickListener {
            entryEvent?.onCloseClicked()
        }

        val closeElement = findViewById<TextView>(R.id.close)
        closeElement.setOnClickListener{
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
