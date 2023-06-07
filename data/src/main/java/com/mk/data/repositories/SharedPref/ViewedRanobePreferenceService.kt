package com.mk.data.repositories.SharedPref

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.mk.domain.Const
import com.mk.domain.Const.VIEWED_SHARED_KEY
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.IRanobe
import com.mk.domain.models.PreviouslyReadRanobeModel
import java.io.StringReader

class ViewedRanobePreferenceService(private val preferenceService: SharedPreferences) {
    var editor: SharedPreferences.Editor = preferenceService.edit()
    val gson = Gson()

    fun getViewedRanobe(): List<PreviouslyReadRanobeModel>? {
        val listFromShared = preferenceService.getStringSet(VIEWED_SHARED_KEY, null)

        if (listFromShared != null) {
            val reader = JsonReader(StringReader(listFromShared.toString()))
            val type = object : TypeToken<List<PreviouslyReadRanobeModel>>() {}.type
            return gson.fromJson(reader, type)

        }
        return null
    }

    fun setViewedRanobe(previouslyReadRanobeModel: PreviouslyReadRanobeModel) {
        val list = getViewedRanobe()
        if (list != null) {
            val convertedSet = list.map { gson.toJson(it) }.toSet().toMutableSet()
            convertedSet += gson.toJson(previouslyReadRanobeModel)
            editor.putStringSet(VIEWED_SHARED_KEY, convertedSet).commit()
        } else {
            editor.putStringSet(VIEWED_SHARED_KEY, setOf(gson.toJson(previouslyReadRanobeModel)))
                .commit()
        }
    }

    fun isViewedRanobe(previouslyReadRanobeModel: IRanobe): Boolean {
        val list = getViewedRanobe()
        return list?.any { it.title == previouslyReadRanobeModel.title } ?: false
    }
}