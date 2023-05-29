package com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mk.data.repositories.ranobes.ReturnRanobeRepositoryImpl
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.LoadRanobePageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RanobeInfoViewModel() : ViewModel() {
    private var _ranobe = MutableLiveData<FullRanobeModel>()
    var ranobe = _ranobe
    private lateinit var url: String

    init {
        CoroutineScope(Dispatchers.IO).launch { loadData() }
    }

    private suspend fun loadData() {
        try {
            val getRanobe = LoadRanobePageUseCase(ReturnRanobeRepositoryImpl())
            val resultRanobe: FullRanobeModel = getRanobe.execute(url)
            _ranobe.postValue(resultRanobe)
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    fun setUrl(url: String) {
        this.url = url
    }
}