package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel
import org.json.JSONObject

interface IRanobeFromSharedPrefRepository {
    suspend fun fetchRanobeList(): MutableList<RanobeModel>
}