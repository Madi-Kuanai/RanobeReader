package com.mk.domain.useCase

import com.mk.domain.models.UpdatedRanobeModel
import org.json.JSONObject

interface IUpdateRanobeRepository {
    suspend fun fetchRanobeList(
        url: String,
    ): MutableList<UpdatedRanobeModel>
}