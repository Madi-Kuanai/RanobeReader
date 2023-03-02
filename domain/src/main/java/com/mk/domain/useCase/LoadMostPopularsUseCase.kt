package com.mk.domain.useCase

import com.mk.domain.Const
import com.mk.domain.models.RanobeModel
import org.json.JSONObject

class LoadMostPopularsUseCase(private val ranobeRepositoryImpl: IRanobeRepository) :
    IReturnListRanobeUseCase {
    override suspend fun execute(page: Int): List<RanobeModel> {
        val payload = JSONObject()
        payload.put("period", "new")
        payload.put("adult", 0)
        print(Const.MOST_POPULAR_URI + Const.NEXT_PAGE_URI + page)
        return ranobeRepositoryImpl.fetchRanobeList(
            Const.MOST_POPULAR_URI + Const.NEXT_PAGE_URI + page,
            Const.GET,
            null
        ) as List<RanobeModel>
    }
}