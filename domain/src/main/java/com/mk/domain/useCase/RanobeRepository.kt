package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel
import org.json.JSONObject

interface RanobeRepository {
    suspend fun fetchRanobeList(url: String, method: String, payload: JSONObject): List<RanobeModel>
}