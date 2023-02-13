package com.mk.ranobereader.Settings

import android.content.SharedPreferences
import com.mk.ranobereader.Const

class PreferenceService(var preferenceService: SharedPreferences) {
    var editor: SharedPreferences.Editor = preferenceService.edit()
    fun setNightTheme() {
        editor.putBoolean(Const.isNightTheme, true).apply()
    }

    fun setLightTheme() {
        editor.putBoolean(Const.isNightTheme, false).apply()
    }

    val isNightTheme: Boolean
        get() = preferenceService.getBoolean(Const.isNightTheme, false)

}