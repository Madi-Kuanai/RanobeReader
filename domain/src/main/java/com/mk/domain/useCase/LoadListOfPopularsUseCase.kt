package com.mk.domain.useCase

import com.mk.domain.Const
import com.mk.domain.models.RanobeModel

class LoadListOfPopularsUseCase(private val ranobeRepository: IRanobeRepository) :
    IReturnListRanobe {
    override suspend fun execute(): List<RanobeModel> {
        return ranobeRepository.fetchRanobeList(Const.MostPopularUri, Const.POST, null)
    }
}