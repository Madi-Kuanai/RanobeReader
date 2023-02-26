package com.mk.ranobereader.presentation.ranobeListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RanobeListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RanobeListViewModel() as T
    }
}