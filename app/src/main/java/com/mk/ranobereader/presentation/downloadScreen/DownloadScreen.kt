package com.mk.ranobereader.presentation.downloadScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mk.ranobereader.R
import android.view.View
import androidx.fragment.app.Fragment

class DownloadScreen : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("download")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //SAVE STATE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download_screen, container, false)
    }
}