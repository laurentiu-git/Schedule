package com.example.schedule.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.platform.app.InstrumentationRegistry

class TestMethods {
    companion object {
        fun clickXY(x: Int, y: Int): ViewAction? {
            return GeneralClickAction(
                Tap.SINGLE,
                { view ->
                    val screenPos = IntArray(2)
                    view.getLocationOnScreen(screenPos)
                    val screenX = (screenPos[0] + x).toFloat()
                    val screenY = (screenPos[1] + y).toFloat()
                    floatArrayOf(screenX, screenY)
                },
                Press.FINGER
            )
        }

        fun isKeyboardShown(): Boolean {
            val inputMethodManager = InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.isAcceptingText
        }
    }
}
