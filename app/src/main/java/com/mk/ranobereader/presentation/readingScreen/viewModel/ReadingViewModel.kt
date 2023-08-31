package com.mk.ranobereader.presentation.readingScreen.viewModel

import android.app.Application
import android.content.Context
import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mk.data.repositories.SharedPref.ReadingSettingPreferenceService
import com.mk.data.repositories.SharedPref.ViewedRanobePreferenceService
import com.mk.data.repositories.ranobes.RanobePageTextRepositoryImpl
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.domain.useCase.LoadRanobePageTextUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReadingViewModel(private val application: Application) : AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var _content = MutableLiveData<List<String>?>()
    var content = _content
    private var _fontSize = MutableLiveData<Int>()
    private var _brightness = MutableLiveData<Int>()
    var fontSize = _fontSize
    var brightness = _brightness
    private var url: String?
    private var viewedSharedPref: ViewedRanobePreferenceService = ViewedRanobePreferenceService(
        application.getSharedPreferences(
            Const.VIEWED_SHARED_KEY, Context.MODE_PRIVATE
        )
    )
    private var readingSharedPref: ReadingSettingPreferenceService =
        ReadingSettingPreferenceService(
            application.getSharedPreferences(
                Const.READING_SETTING_KEY, Context.MODE_PRIVATE
            )
        )


    init {
        url = viewedSharedPref.getLastOpenedRanobe()?.lastChapterLink
        if (readingSharedPref.getFontSize() != -1) {
            _fontSize.postValue(readingSharedPref.getFontSize())
            Log.d(TAG, "value: ${readingSharedPref.getFontSize()} ")
        }
        if (readingSharedPref.getBrightness() != -1) _brightness.postValue(readingSharedPref.getBrightness())
    }

    internal fun getPageText(url: String) {
        Log.d(TAG, "getPageText: $url")
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handleNetworkError(throwable)
        }

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                parseText(url)
            }
        }
    }

    fun setBrightness(brightness: Int) {
        readingSharedPref.setBrightness(brightness)
    }

    fun setFontSize(fontSize: Int) {
        readingSharedPref.setFontSize(fontSize)
    }

    private suspend fun parseText(url: String) {
        try {
            val getRanobeText = LoadRanobePageTextUseCase(RanobePageTextRepositoryImpl())
            val result = getRanobeText.execute(url)
            if (result != null) {
                _content.postValue(result)
            }
        } catch (e: Exception) {
            Log.d(TAG, "parseText: $e")
        }
    }

    fun saveLastOpenedRanobe(previouslyReadRanobeModel: PreviouslyReadRanobeModel) {
        viewedSharedPref.saveLastOpenedRanobe(previouslyReadRanobeModel)
    }

    fun getLastOpenedRanobe(): PreviouslyReadRanobeModel? {
        Log.d(TAG, "getLastOpenedRanobe: ")
        return viewedSharedPref.getLastOpenedRanobe()
    }

    fun removeLastOpenedRanobe() {
        viewedSharedPref.removeLastOpenedRanobe()
    }

    private fun handleNetworkError(throwable: Throwable) {
        // Handle network error
    }

    private fun goNextPage() {

    }
}