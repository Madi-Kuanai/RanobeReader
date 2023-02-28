package com.mk.domain.useCase


import com.mk.domain.Const
import com.mk.domain.models.RanobeModel

import org.json.JSONObject

class LoadMostViewedUseCase(
    private val IRanobeRepository: IRanobeRepository,
) : IReturnListRanobe {
    override suspend fun execute(page: Int): List<RanobeModel> {
        val payload = JSONObject()
        payload.put("period", "new")
        payload.put("adult", 0)
        print(Const.MOST_VIEWED_URI + Const.NEXT_PAGE_URI + page)
        return IRanobeRepository.fetchRanobeList(
            Const.MOST_VIEWED_URI + Const.NEXT_PAGE_URI + page,
            Const.GET,
            null
        )
    }
}