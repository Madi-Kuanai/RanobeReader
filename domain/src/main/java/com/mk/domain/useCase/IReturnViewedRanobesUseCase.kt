package com.mk.domain.useCase

import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.domain.models.RanobeModel

interface IReturnViewedRanobesUseCase {
    suspend fun execute(): MutableList<RanobeModel>

}