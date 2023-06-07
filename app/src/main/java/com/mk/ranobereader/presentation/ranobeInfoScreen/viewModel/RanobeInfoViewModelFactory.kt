package com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mk.data.repositories.SharedPref.ViewedRanobePreferenceService

class RanobeInfoViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RanobeInfoViewModel(application) as T
    }
}