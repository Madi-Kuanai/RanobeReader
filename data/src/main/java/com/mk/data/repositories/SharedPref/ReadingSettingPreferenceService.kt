package com.mk.data.repositories.SharedPref

import android.content.SharedPreferences
import android.util.Log
import com.mk.domain.Const
import com.mk.domain.Const.TAG

class ReadingSettingPreferenceService(private val preferenceService: SharedPreferences) {
    private val editor: SharedPreferences.Editor = preferenceService.edit()

    fun getFontSize(): Int {
        Log.d(TAG, "getFontSize: ${preferenceService.getInt(Const.FONT_SIZE, -1)}")
        return preferenceService.getInt(Const.FONT_SIZE, -1)
    }

    fun setFontSize(fontSize: Int) {
        editor.putInt(Const.FONT_SIZE, fontSize).commit()
    }

    fun getBrightness(): Int {
        Log.d(TAG, "getFontSize: ${preferenceService.getInt(Const.BRIGHTNESS, -1)}")
        return preferenceService.getInt(Const.BRIGHTNESS, -1)
    }

    fun setBrightness(brightness: Int) {
        Log.d(Const.TAG, "setBrightness 1: $brightness")
        editor.putInt(Const.BRIGHTNESS, brightness).commit()
    }

}