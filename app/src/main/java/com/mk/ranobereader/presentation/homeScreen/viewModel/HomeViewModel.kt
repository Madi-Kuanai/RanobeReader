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
import com.mk.data.repositories.SharedPref.ViewedRanobePreferenceService
import com.mk.data.repositories.ranobes.RanobeFromListRepositoryImpl
import com.mk.data.repositories.ranobes.UpdateRanobeRepositoryImp
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.domain.models.RanobeModel
import com.mk.domain.models.UpdatedRanobeModel
import com.mk.domain.useCase.LoadMostPopularsUseCase
import com.mk.domain.useCase.LoadMostViewedUseCase
import com.mk.domain.useCase.LoadUpdatedRanobeUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.json.JSONException
import java.util.concurrent.ExecutionException

class HomeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _mvWithDescriptionRanobeModel = MutableLiveData<ArrayList<RanobeModel>?>()
    private val _mvMostViewedLayoutPosition = MutableLiveData<Float>()
    private val _mvPopularsRanobe = MutableLiveData<ArrayList<RanobeModel>?>()
    private val _mvUpdatesRanobe = MutableLiveData<ArrayList<UpdatedRanobeModel>>()
    private val _mvUpdatesPosition = MutableLiveData<Float>()
    private val _mvPopularsPosition = MutableLiveData<Float>()
    private val _viewedRanobe = MutableLiveData<ArrayList<PreviouslyReadRanobeModel>>()

    val mvWithDescriptionRanobeModel: MutableLiveData<ArrayList<RanobeModel>?> =
        _mvWithDescriptionRanobeModel
    val mvMostViewedLayoutPosition = _mvMostViewedLayoutPosition
    val mvPopularsRanobeModel: MutableLiveData<ArrayList<RanobeModel>?> = _mvPopularsRanobe
    val mvUpdatesRanobeModel = _mvUpdatesRanobe
    val mvUpdatesPosition = _mvUpdatesPosition
    val mvPopularsPosition = _mvPopularsPosition
    val viewedRanobe = _viewedRanobe
    private var sharedPref: ViewedRanobePreferenceService = ViewedRanobePreferenceService(
        application.getSharedPreferences(
            Const.VIEWED_SHARED_KEY,
            Context.MODE_PRIVATE
        )
    )

    init {
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
            val viewedRanobe: List<PreviouslyReadRanobeModel>? = getViewedRanobes()
            if (viewedRanobe != null) {
                _viewedRanobe.postValue(viewedRanobe as ArrayList<PreviouslyReadRanobeModel>?)
            }
            val ranobeFromListRepository = RanobeFromListRepositoryImpl()
            val updateRanobeRepository = UpdateRanobeRepositoryImp()

            val getMostViewed = LoadMostViewedUseCase(ranobeFromListRepository)
            val getMostPopular = LoadMostPopularsUseCase(ranobeFromListRepository)
            val getUpdates = LoadUpdatedRanobeUseCase(updateRanobeRepository)

            val resultUpdates: List<UpdatedRanobeModel> = getUpdates.execute(1)
            _mvUpdatesRanobe.postValue(resultUpdates as ArrayList<UpdatedRanobeModel>)

            val resultMostPopular: List<RanobeModel> = getMostPopular.execute(1)
            _mvPopularsRanobe.postValue(resultMostPopular as ArrayList<RanobeModel>)

            val resultMostViewed: List<RanobeModel> = getMostViewed.execute(1)
            _mvWithDescriptionRanobeModel.postValue(resultMostViewed as ArrayList<RanobeModel>)

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
        try {
            _mvPopularsRanobe.postValue(null)
            _mvMostViewedLayoutPosition.postValue(0.0f)
            _mvPopularsPosition.postValue(0.0f)
            _mvUpdatesPosition.postValue(0.0f)
            _mvWithDescriptionRanobeModel.postValue(null)
            viewModelScope.launch(Dispatchers.IO) {
                loadData()
            }
        } catch (e: ExecutionException) {
            Log.d(TAG, "refreshData: ${e.stackTrace}")
            handleDataLoadingError(e)
        } catch (e: InterruptedException) {

            Log.d(TAG, "refreshData: ${e.stackTrace}")
            handleDataLoadingError(e)
        } catch (e: JSONException) {
            Log.d(TAG, "refreshData: ${e.stackTrace}")
            handleDataLoadingError(e)
        }
    }

//    fun setViewedRanobe(nRanobeModel: PreviouslyReadRanobeModel) {
//        sharedPref.setViewedRanobe(nRanobeModel)
//    }
//
//    fun isViewed(nRanobeModel: IRanobe): Boolean {
//        return sharedPref.isViewedRanobe(nRanobeModel)
//    }

    private fun getViewedRanobes(): List<PreviouslyReadRanobeModel>? {
        return sharedPref.getViewedRanobesList()
    }
}
