package com.mk.ranobereader.presentation.ranobeListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mk.domain.useCase.IReturnListRanobeUseCase

class RanobeListViewModelFactory(private val iReturnListRanobeUseCase: IReturnListRanobeUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RanobeListViewModel(iReturnListRanobeUseCase) as T
    }
}