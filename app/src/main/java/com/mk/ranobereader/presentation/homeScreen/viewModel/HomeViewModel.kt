package com.mk.ranobereader.presentation.homeScreen.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mk.data.repositories.ranobes.RanobeRepositoryImpl
import com.mk.data.repositories.ranobes.UpdateRanobeRepositoryImp
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.domain.models.UpdatedRanobeModel
import com.mk.domain.useCase.LoadMostPopularsUseCase
import com.mk.domain.useCase.LoadMostViewedUseCase
import com.mk.domain.useCase.LoadUpdatedRanobeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.concurrent.ExecutionException

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private var _mvWithDescriptionCard = MutableLiveData<ArrayList<RanobeModel>?>()
    private var _mvMostViewedLayoutPosition = MutableLiveData<Float>()
    private var _mvPopulars = MutableLiveData<ArrayList<RanobeModel>?>()
    private var _mvUpdates = MutableLiveData<ArrayList<UpdatedRanobeModel>>()
    internal val mvLst: MutableLiveData<ArrayList<RanobeModel>?> = _mvWithDescriptionCard
    internal val mvMostViewedLayoutPosition = _mvMostViewedLayoutPosition
    internal val mvPopulars: MutableLiveData<ArrayList<RanobeModel>?> = _mvPopulars
    internal val mvUpdates = _mvUpdates

    init {
        Log.d(TAG, "ViewModel Create")
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            loadData()
        }

        viewModelScope.launch(Dispatchers.IO) {
            UpdateRanobeRepositoryImp().fetchRanobeList(
                "https://tl.rulate.ru/site/login?page=",

            )
        }
    }


    private suspend fun loadData() {
        try {
            val getMostViewed = LoadMostViewedUseCase(RanobeRepositoryImpl())
            val getMostPopular = LoadMostPopularsUseCase(RanobeRepositoryImpl())
            val getUpdates = LoadUpdatedRanobeUseCase(UpdateRanobeRepositoryImp())
            val resultMostViewed: List<RanobeModel> = getMostViewed.execute(1)
            val resultMostPopular: List<RanobeModel> = getMostPopular.execute(1)
            val resultUpdates: List<UpdatedRanobeModel> = getUpdates.execute(1)
            _mvPopulars.postValue(resultMostPopular as ArrayList<RanobeModel>)
            _mvWithDescriptionCard.postValue(resultMostViewed as ArrayList<RanobeModel>)
            _mvUpdates.postValue(resultUpdates as ArrayList<UpdatedRanobeModel>)
        } catch (e: ExecutionException) {
            // Log.d(TAG, "Error in init" + e.message)
        } catch (e: InterruptedException) {
            // Log.d(TAG, "Error in init" + e.message)
        } catch (e: JSONException) {
            // Log.d(TAG, "Error in init" + e.message)
        }
    }

    internal fun refreshData() {
        _mvPopulars.postValue(null)
        _mvMostViewedLayoutPosition.postValue(0.0f)
        _mvWithDescriptionCard.postValue(null)
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            loadData()
        }
    }
}