package com.mk.domain.useCase

import com.mk.domain.Const
import com.mk.domain.models.UpdatedRanobeModel

class LoadUpdatedRanobeUseCase(private val iUpdateRanobeRepository: IUpdateRanobeRepository) {
    suspend fun execute(page: Int): MutableList<UpdatedRanobeModel> {
        return iUpdateRanobeRepository.fetchRanobeList(Const.MAIN_PAGE_LINK + page)
    }
}