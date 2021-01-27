package com.example.schedule.ui.transitions

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
        closeElement.setOnClickListener {
            entryEvent?.onCloseClicked()
        }

        val addLayout = findViewById<ConstraintLayout>(R.id.addLayout)
        addLayout.setOnClickListener {
            hideKeyboardFrom(context, it)
        }
    }

    fun setAddEventListener(entryEventListener: EntryEventListener) {
        this.entryEvent = entryEventListener
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

interface EntryEventListener {

    fun onCloseClicked()
}
