package com.mk.domain.useCase


import com.mk.domain.Const
import com.mk.domain.models.RanobeModel

import org.json.JSONObject

class LoadMostViewedUseCase(private val IRanobeRepository: IRanobeRepository) {
    suspend fun execute(): List<RanobeModel> {
        val payload = JSONObject()
        payload.put("period", "new")
        payload.put("adult", 0)
        return IRanobeRepository.fetchRanobeList(Const.MostPopularUri, Const.POST, payload)
    }
}