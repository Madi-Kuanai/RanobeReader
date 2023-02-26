package com.mk.ranobereader.presentation.homeScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mk.core.Const.TAG
import com.mk.data.repositories.ranobes.RanobeRepositoryImpl
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.LoadMostPopularsUseCase
import com.mk.domain.useCase.LoadMostViewedUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.concurrent.ExecutionException

class HomeViewModel() : ViewModel() {
    private var _mvWithDescriptionCard = MutableLiveData<ArrayList<RanobeModel>?>()
    private var _mvMostViewedLayoutPosition = MutableLiveData<Float>()
    private var _mvPopulars = MutableLiveData<ArrayList<RanobeModel>?>()
    internal val mvLst: MutableLiveData<ArrayList<RanobeModel>?> = _mvWithDescriptionCard
    internal val mvMostViewedLayoutPosition = _mvMostViewedLayoutPosition
    internal val mvPopulars: MutableLiveData<ArrayList<RanobeModel>?> = _mvPopulars

    init {
        Log.d(TAG, "ViewModel Create")
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            initTop()
        }

    }


    private suspend fun initTop() {
        try {
            val getMostViewed = LoadMostViewedUseCase(RanobeRepositoryImpl())
            val getMostPopular = LoadMostPopularsUseCase(RanobeRepositoryImpl())
            val resultMostViewed: List<RanobeModel> = getMostViewed.execute()
            val resultMostPopular: List<RanobeModel> = getMostPopular.execute()
            _mvPopulars.postValue(resultMostPopular as ArrayList<RanobeModel>)
            _mvWithDescriptionCard.postValue(resultMostViewed as ArrayList<RanobeModel>)
        } catch (e: ExecutionException) {
            Log.d(TAG, "Error in init" + e.message)
        } catch (e: InterruptedException) {
            Log.d(TAG, "Error in init" + e.message)
        } catch (e: JSONException) {
            Log.d(TAG, "Error in init" + e.message)
        }
    }
}