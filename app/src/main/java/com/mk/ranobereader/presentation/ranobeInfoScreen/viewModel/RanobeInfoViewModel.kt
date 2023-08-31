package com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mk.data.repositories.SharedPref.ViewedRanobePreferenceService
import com.mk.data.repositories.ranobes.ReturnRanobeRepositoryImpl
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.IRanobe
import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.domain.useCase.LoadRanobeInfoPageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RanobeInfoViewModel(private val application: Application) : AndroidViewModel(application) {
    private var _ranobe = MutableLiveData<FullRanobeModel>()
    var ranobe = _ranobe
    private lateinit var url: String
    private var sharedPref: ViewedRanobePreferenceService = ViewedRanobePreferenceService(
        application.getSharedPreferences(
            Const.VIEWED_SHARED_KEY,
            MODE_PRIVATE
        )
    )
    var initRanobeModel = MutableLiveData<IRanobe>()

    fun getData(url: String) {
        if (url.contains("https://tl.rulate.ruhttps://tl.rulate.ru")) {
            this.url = url.replace("https://tl.rulate.ruhttps://tl.rulate.ru", "")
        } else {
            this.url = url
        }
        Log.d(TAG, url)
        CoroutineScope(Dispatchers.IO).launch { loadData() }
    }

    private suspend fun loadData() {
        try {
            val getRanobe = LoadRanobeInfoPageUseCase(ReturnRanobeRepositoryImpl())
            val resultRanobe: FullRanobeModel = getRanobe.execute(url)
            _ranobe.postValue(resultRanobe)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }


    fun setViewedRanobe(nRanobeModel: PreviouslyReadRanobeModel) {
        sharedPref.setViewedRanobe(nRanobeModel)
    }

    fun isViewedRanobe(nRanobeModel: IRanobe): Boolean {
        return sharedPref.isViewedRanobe(nRanobeModel)
    }

    fun getViewedRanobe(iRanobe: IRanobe): PreviouslyReadRanobeModel? {
        return sharedPref.getViewedRanobe(iRanobe)
    }
}