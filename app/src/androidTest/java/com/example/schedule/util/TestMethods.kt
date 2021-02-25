package com.example.schedule.util

import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap


class TestMethods {
 companion object{
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
 }
}