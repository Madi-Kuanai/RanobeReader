package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel
import com.mk.domain.models.UpdatedRanobeModel
import org.json.JSONObject

interface IRanobeFromListRepository {
    suspend fun fetchRanobeList(
        url: String,
        method: String,
        payload: JSONObject?
    ): MutableList<RanobeModel>
}