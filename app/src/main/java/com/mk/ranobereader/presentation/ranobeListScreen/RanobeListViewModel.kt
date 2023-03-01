package com.mk.ranobereader.presentation.ranobeListScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mk.domain.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.IReturnListRanobeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RanobeListViewModel(private var iReturnListRanobeUseCase: IReturnListRanobeUseCase) :
    ViewModel() {
    private val _listRanobe = MutableLiveData<Set<RanobeModel>?>()
    val listRanobe: MutableLiveData<Set<RanobeModel>?> = _listRanobe
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
    }

    private suspend fun loadData() {
        Log.d(TAG, "LoadData")
        pageOfList.value?.let { it ->
            iReturnListRanobeUseCase.execute(it).let {
                Log.d(TAG, it.toString())
                it.forEach { ranobeModel ->
                    _listRanobe.postValue(it.toSet())
                }
            }
        }
    }

    internal fun refreshData() {
        _listRanobe.postValue(null)
        pageOfList.postValue(1)
        screenPos.postValue(0)
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            loadData()
        }
    }
}