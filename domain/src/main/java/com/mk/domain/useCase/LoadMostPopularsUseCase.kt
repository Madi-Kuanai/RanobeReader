package com.mk.domain.useCase

import com.mk.domain.Const
import com.mk.domain.models.RanobeModel
import org.json.JSONObject

class LoadMostPopularsUseCase(private val ranobeRepositoryImpl: IRanobeRepository) : IReturnListRanobe {
    override suspend fun execute(): List<RanobeModel> {
        val payload = JSONObject()
        payload.put("period", "new")
        payload.put("adult", 0)
        return ranobeRepositoryImpl.fetchRanobeList(Const.mostViewedUri, Const.GET, null)
    }
}