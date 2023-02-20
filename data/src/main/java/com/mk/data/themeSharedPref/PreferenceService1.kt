package com.mk.data.themeSharedPref

import android.content.SharedPreferences
import com.mk.core.Const

class PreferenceService(var preferenceService: SharedPreferences) {
    var editor: SharedPreferences.Editor = preferenceService.edit()
    fun setNightTheme() {
        editor.putBoolean(com.mk.core.Const.isNightTheme, true).apply()
    }

    fun setLightTheme() {
        editor.putBoolean(com.mk.core.Const.isNightTheme, false).apply()
    }

    val isNightTheme: Boolean
        get() = preferenceService.getBoolean(com.mk.core.Const.isNightTheme, false)

}