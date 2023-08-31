package com.mk.ranobereader.presentation.readingScreen.listener

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.mk.domain.Const.TAG
//
//class CustomTouchListener : GestureDetector.SimpleOnGestureListener() {
//    override fun onDown(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                val screenWidth = view.width
//                val screenHeight = view.height
//
//                val touchX = event.x
//                val touchY = event.y
//
//                val leftThreshold = screenWidth / 4
//                val rightThreshold = 3 * screenWidth / 4
//
//                when {
//                    touchX < leftThreshold -> {
//                        Log.d(TAG, "onTouch: left")
//                    }
//
//                    touchX > rightThreshold -> {
//                        Log.d(TAG, "onTouch: right")
//                    }
//
//                    else -> {
//                        Log.d(TAG, "onTouch: center")
//                    }
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                // Игнорируем события ACTION_MOVE
//            }
//        }
//        return true
//    }
//}
