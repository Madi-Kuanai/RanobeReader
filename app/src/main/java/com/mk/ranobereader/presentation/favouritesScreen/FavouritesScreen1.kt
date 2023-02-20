package com.mk.ranobereader.presentation.favouritesScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mk.ranobereader.R
import android.view.View
import androidx.fragment.app.Fragment

class FavouritesScreen : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites_screen, container, false)
    }
}