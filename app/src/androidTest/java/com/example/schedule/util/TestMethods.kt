package com.example.schedule.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.EasyMock2Matchers.equalTo
import org.hamcrest.Matcher

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

        fun getText(matcher: ViewInteraction): String {
            var text = String()
            matcher.perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(TextView::class.java)
                }

                override fun getDescription(): String {
                    return "Text of the view"
                }

                override fun perform(uiController: UiController, view: View) {
                    val tv = view as TextView
                    text = tv.text.toString()
                }
            })

            return text
        }

        fun withItemContent(expectedText: String?): Matcher<Any?>? {
            checkNotNull(expectedText)
            return withItemContent(equalTo(expectedText))
        }
    }
}
