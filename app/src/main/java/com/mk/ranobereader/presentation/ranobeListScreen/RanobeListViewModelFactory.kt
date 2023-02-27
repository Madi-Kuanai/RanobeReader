package com.mk.ranobereader.presentation.ranobeListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mk.domain.useCase.IReturnListRanobe

class RanobeListViewModelFactory(private val iReturnListRanobe: IReturnListRanobe) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RanobeListViewModel(iReturnListRanobe) as T
    }
}