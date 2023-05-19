package com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RanobeInfoViewModelFactory() :
    ViewModelProvider.AndroidViewModelFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RanobeInfoViewModel() as T
    }
}