package com.mk.domain.useCase

import com.mk.domain.Const
import com.mk.domain.models.RanobeModel

class LoadListOfPopularsUseCase(private val ranobeRepository: IRanobeRepository) :
    IReturnListRanobeUseCase {
    override suspend fun execute(page: Int): List<RanobeModel> {
        print(Const.MOST_POPULAR_URI + Const.NEXT_PAGE_URI + page)
        return ranobeRepository.fetchRanobeList(
            Const.MOST_POPULAR_URI + Const.NEXT_PAGE_URI + page,
            Const.GET,
            null
        )
    }
}