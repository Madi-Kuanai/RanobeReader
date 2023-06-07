package com.mk.domain.useCase

import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.domain.models.RanobeModel

class LoadViewedRanobesUseCase(private val returnViewed: IRanobeFromSharedPrefRepository) :
    IReturnViewedRanobesUseCase {
    override suspend fun execute(): MutableList<RanobeModel> {
        return returnViewed.fetchRanobeList()
    }

}