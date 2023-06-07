package com.mk.data.repositories.SharedPref

import android.content.SharedPreferences
import com.mk.domain.Const

class ThemePreferenceService(var preferenceService: SharedPreferences) {
    var editor: SharedPreferences.Editor = preferenceService.edit()
    fun setNightTheme() {
        editor.putBoolean(Const.IS_NIGHT_THEME, true).apply()
    }

    fun setLightTheme() {
        editor.putBoolean(Const.IS_NIGHT_THEME, false).apply()
    }

    val isNightTheme: Boolean
        get() = preferenceService.getBoolean(Const.IS_NIGHT_THEME, false)
}