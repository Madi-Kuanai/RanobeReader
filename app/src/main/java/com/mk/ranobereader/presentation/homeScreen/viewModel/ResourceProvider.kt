package com.mk.ranobereader.presentation.homeScreen.viewModel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.FragmentHomeScreenBinding

class ResourceProvider(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val binding: FragmentHomeScreenBinding
) {
    fun getRanobeCard(): Int {
        return R.layout.ranobe_card_with_description
    }

    fun getView(): View? {
        return layoutInflater.inflate(
            getRanobeCard(), binding.root,
            false
        )
    }

    fun getLayout(): LinearLayoutManager {
        return LinearLayoutManager(this.context)
    }
}
