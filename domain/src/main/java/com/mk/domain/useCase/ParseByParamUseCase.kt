package com.mk.domain.useCase


import com.mk.domain.Const
import com.mk.domain.models.RanobeModel

import org.json.JSONObject

class ParseByParamUseCase(private val ranobeRepository: RanobeRepository) {
    suspend fun execute(): List<RanobeModel> {
        val payload = JSONObject()
        payload.put("period", "new")
        payload.put("adult", 0)

        return ranobeRepository.fetchRanobeList(Const.TopsUri, Const.POST, payload);
    }
}