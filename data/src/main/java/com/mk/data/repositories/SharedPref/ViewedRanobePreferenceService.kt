package com.mk.data.repositories.SharedPref

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.Const.VIEWED_SHARED_KEY
import com.mk.domain.models.IRanobe
import com.mk.domain.models.PreviouslyReadRanobeModel
import java.io.StringReader
import kotlin.io.path.fileVisitor

class ViewedRanobePreferenceService(private val preferenceService: SharedPreferences) {
    var editor: SharedPreferences.Editor = preferenceService.edit()
    val gson = Gson()

    fun getViewedRanobesList(): List<PreviouslyReadRanobeModel>? {
        val listFromShared = preferenceService.getStringSet(VIEWED_SHARED_KEY, null)

        if (listFromShared != null) {
            val reader = JsonReader(StringReader(listFromShared.toString()))
            val type = object : TypeToken<List<PreviouslyReadRanobeModel>>() {}.type
            return gson.fromJson(reader, type)

        }
        return null
    }

    fun saveLastOpenedRanobe(previouslyReadRanobeModel: PreviouslyReadRanobeModel) {
        editor.putString(Const.LAST_OPENED_RANOBE, gson.toJson(previouslyReadRanobeModel)).commit()
    }

    fun getLastOpenedRanobe(): PreviouslyReadRanobeModel? {
        val fromShared = preferenceService.getString(Const.LAST_OPENED_RANOBE, null)
        if (fromShared != null) {
            val reader = JsonReader(StringReader(fromShared.toString()))
            val type = object : TypeToken<PreviouslyReadRanobeModel>() {}.type
            val ranobe: PreviouslyReadRanobeModel = gson.fromJson(reader, type)
            Log.d(TAG, "getLastOpenedRanobe: $ranobe")
            return ranobe
        }
        return null
    }

    fun removeLastOpenedRanobe() {
        editor.remove(Const.LAST_OPENED_RANOBE).commit()
    }

    fun setViewedRanobe(previouslyReadRanobeModel: PreviouslyReadRanobeModel) {
        var list = getViewedRanobesList()
        if (list != null) {
            if (getViewedRanobe(previouslyReadRanobeModel as IRanobe) != null) {
                val newList = arrayListOf<PreviouslyReadRanobeModel>()
                list.forEach { it ->
                    if (it.title != previouslyReadRanobeModel.title) {
                        newList.add(it)
                    } else {
                        Log.d(TAG, "setViewedRanobe: In previous list")
                    }
                }
                list = newList
            }
            val convertedSet = list.map { gson.toJson(it) }.toSet().toMutableSet()
            convertedSet += gson.toJson(previouslyReadRanobeModel)
            editor.putStringSet(VIEWED_SHARED_KEY, convertedSet).commit()
        } else {
            editor.putStringSet(VIEWED_SHARED_KEY, setOf(gson.toJson(previouslyReadRanobeModel)))
                .commit()
        }
    }

    fun isViewedRanobe(previouslyReadRanobeModel: IRanobe): Boolean {
        val list = getViewedRanobesList()
        return list?.any { it.title == previouslyReadRanobeModel.title } ?: false
    }

    fun removeViewedRanobe(previouslyReadRanobeModel: IRanobe) {

    }

    fun getViewedRanobe(iRanobe: IRanobe): PreviouslyReadRanobeModel? {
        return if (isViewedRanobe(iRanobe)) {
            Log.d(TAG, getViewedRanobesList()?.filter { it.title == iRanobe.title }.toString())
            getViewedRanobesList()?.first { it.title == iRanobe.title }
        } else {
            null
        }
    }
}