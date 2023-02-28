package com.mk.ranobereader.presentation.ranobeListScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mk.domain.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.IReturnListRanobe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RanobeListViewModel(private var iReturnListRanobe: IReturnListRanobe) : ViewModel() {
    private val _listRanobe = MutableLiveData<List<RanobeModel>>()
    val listRanobe: MutableLiveData<List<RanobeModel>> = _listRanobe
    val screenPos = MutableLiveData<Int>()
    var pageOfList = MutableLiveData<Int>(1)

    init {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            loadData()
        }
        pageOfList.observeForever {
            coroutineScope.launch {
                loadData()
            }
        }
        screenPos.observeForever {
            Log.d(TAG, it.toString())
        }
    }

    private suspend fun loadData() {
        Log.d(TAG, "LoadData")
        pageOfList.value?.let { it ->
            iReturnListRanobe.execute(it).let {
                Log.d(TAG, it.toString())
                _listRanobe.postValue(it)
            }
        }
    }
}