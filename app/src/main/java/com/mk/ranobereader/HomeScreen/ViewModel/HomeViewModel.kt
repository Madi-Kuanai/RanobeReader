package com.mk.ranobereader.HomeScreen.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mk.ranobereader.Const
import com.mk.ranobereader.Const.TAG
import com.mk.ranobereader.backend.HomeInformation.RanobeListParser
import com.mk.ranobereader.models.RanobeModel
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ExecutionException

class HomeViewModel() : ViewModel() {
    private var _mvWithDescriptionCard = MutableLiveData<ArrayList<RanobeModel>?>();
    val mvLst: MutableLiveData<ArrayList<RanobeModel>?> = _mvWithDescriptionCard

    init {
        Log.d(TAG, "ViewModel Create")
        initTop()
    }

    private fun initTop() {
        try {
            val getMostLiked = RanobeListParser()
            val payload = JSONObject()
            payload.put("period", "new")
            payload.put("adult", 0)
            getMostLiked.execute(Const.TopsUri, Const.POST, payload.toString())
            val result: ArrayList<RanobeModel>? = getMostLiked.get() as ArrayList<RanobeModel>?
            if (result != null) {
                _mvWithDescriptionCard.value = result
            }
        } catch (e: ExecutionException) {
            Log.d(TAG, "Error in init" + e.message)
        } catch (e: InterruptedException) {
            Log.d(TAG, "Error in init" + e.message)
        } catch (e: JSONException) {
            Log.d(TAG, "Error in init" + e.message)
        }
    }
}