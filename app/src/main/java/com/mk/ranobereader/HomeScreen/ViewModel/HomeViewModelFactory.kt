package com.mk.ranobereader.HomeScreen.ViewModel

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mk.ranobereader.Const
import com.mk.ranobereader.HomeScreen.GenresAdapter
import com.mk.ranobereader.R
import com.mk.ranobereader.backend.HomeInformation.RanobeListParser
import com.mk.ranobereader.models.RanobeModel
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ExecutionException

class HomeViewModelFactory(

) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel() as T;
    }
}