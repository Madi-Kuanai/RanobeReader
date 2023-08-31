package com.mk.ranobereader.presentation.readingScreen.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mk.ranobereader.presentation.homeScreen.viewModel.HomeViewModel

class ReadingViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReadingViewModel(application = application) as T;
    }
}