package com.mk.ranobereader.presentation.ranobeListScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.IReturnListRanobe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RanobeListViewModel(private var iReturnListRanobe: IReturnListRanobe) : ViewModel() {
    private val _listRanobe = MutableLiveData<List<RanobeModel>>()
    public val listRanobe: MutableLiveData<List<RanobeModel>> = _listRanobe

    init {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        iReturnListRanobe.execute().let {
            _listRanobe.postValue(it)
        }
    }
}