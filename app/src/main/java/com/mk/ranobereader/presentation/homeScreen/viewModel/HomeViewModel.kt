package com.mk.ranobereader.presentation.homeScreen.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mk.data.repositories.ranobes.RanobeFromListRepositoryImpl
import com.mk.data.repositories.ranobes.UpdateRanobeRepositoryImp
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.domain.models.UpdatedRanobeModel
import com.mk.domain.useCase.LoadMostPopularsUseCase
import com.mk.domain.useCase.LoadMostViewedUseCase
import com.mk.domain.useCase.LoadUpdatedRanobeUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.concurrent.ExecutionException

class HomeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _mvWithDescriptionCard = MutableLiveData<ArrayList<RanobeModel>?>()
    private val _mvMostViewedLayoutPosition = MutableLiveData<Float>()
    private val _mvPopulars = MutableLiveData<ArrayList<RanobeModel>?>()
    private val _mvUpdates = MutableLiveData<ArrayList<UpdatedRanobeModel>>()
    private val _mvUpdatesPosition = MutableLiveData<Float>()
    private val _mvPopularsPosition = MutableLiveData<Float>()

    val mvWithDescription: MutableLiveData<ArrayList<RanobeModel>?> = _mvWithDescriptionCard
    val mvMostViewedLayoutPosition = _mvMostViewedLayoutPosition
    val mvPopulars: MutableLiveData<ArrayList<RanobeModel>?> = _mvPopulars
    val mvUpdates = _mvUpdates
    val mvUpdatesPosition = _mvUpdatesPosition
    val mvPopularsPosition = _mvPopularsPosition

    init {
        Log.d(Const.TAG, "ViewModel Create")
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handleNetworkError(throwable)
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            loadData()
            UpdateRanobeRepositoryImp().fetchRanobeList(getLoginUrl())
        }
    }

    private suspend fun loadData() {
        try {
            val ranobeFromListRepository = RanobeFromListRepositoryImpl()
            val updateRanobeRepository = UpdateRanobeRepositoryImp()

            val getMostViewed = LoadMostViewedUseCase(ranobeFromListRepository)
            val getMostPopular = LoadMostPopularsUseCase(ranobeFromListRepository)
            val getUpdates = LoadUpdatedRanobeUseCase(updateRanobeRepository)

            val resultMostViewed: List<RanobeModel> = getMostViewed.execute(1)
            val resultMostPopular: List<RanobeModel> = getMostPopular.execute(1)
            val resultUpdates: List<UpdatedRanobeModel> = getUpdates.execute(1)

            _mvPopulars.postValue(resultMostPopular as ArrayList<RanobeModel>)
            _mvWithDescriptionCard.postValue(resultMostViewed as ArrayList<RanobeModel>)
            _mvUpdates.postValue(resultUpdates as ArrayList<UpdatedRanobeModel>)
        } catch (e: ExecutionException) {
            Log.d(TAG, "loadData: ${e.stackTrace}")
            handleDataLoadingError(e)
        } catch (e: InterruptedException) {

            Log.d(TAG, "loadData: ${e.stackTrace}")
            handleDataLoadingError(e)
        } catch (e: JSONException) {
            Log.d(TAG, "loadData: ${e.stackTrace}")
            handleDataLoadingError(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    private fun getLoginUrl(): String {
        // Replace with a secure method to obtain the login URL
        return "https://tl.rulate.ru/site/login?page="
    }

    private fun handleNoNetworkError() {
        // Handle no network error
    }

    private fun handleNetworkError(throwable: Throwable) {
        // Handle network error
    }

    private fun handleDataLoadingError(throwable: Throwable) {
        // Handle data loading error
    }

    internal fun refreshData() {
        _mvPopulars.postValue(null)
        _mvMostViewedLayoutPosition.postValue(0.0f)
        _mvPopularsPosition.postValue(0.0f)
        _mvUpdatesPosition.postValue(0.0f)
        _mvWithDescriptionCard.postValue(null)
        viewModelScope.launch(Dispatchers.IO) {
            loadData()
        }
    }
}
