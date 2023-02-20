package com.mk.ranobereader.presentation.homeScreen.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mk.core.Const.TAG
import com.mk.data.repositories.ranobes.RanobeRepositoryImpl
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.ParseByParamUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import java.util.concurrent.ExecutionException

class HomeViewModel() : ViewModel() {
    private var _mvWithDescriptionCard = MutableLiveData<ArrayList<RanobeModel>?>()
    private var _mvLayoutPos = MutableLiveData<Float>()
    val mvLst: MutableLiveData<ArrayList<RanobeModel>?> = _mvWithDescriptionCard
    val mvLayoutPos = _mvLayoutPos

    init {
        Log.d(TAG, "ViewModel Create")
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            initTop()
        }

    }


    private suspend fun initTop() {
        try {
            val ranobeRepositoryImpl = RanobeRepositoryImpl()
            val getMostLiked = ParseByParamUseCase(ranobeRepositoryImpl)

            val result: List<RanobeModel> = getMostLiked.execute()
            _mvWithDescriptionCard.postValue(result as ArrayList<RanobeModel>)
        } catch (e: ExecutionException) {
            Log.d(TAG, "Error in init" + e.message)
        } catch (e: InterruptedException) {
            Log.d(TAG, "Error in init" + e.message)
        } catch (e: JSONException) {
            Log.d(TAG, "Error in init" + e.message)
        }
    }
}