package com.mk.domain.useCase

import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.RanobeModel
import java.net.URL

interface IReturnRanobeRepository {
    suspend fun fetchRanobe(
        url: String
    ): FullRanobeModel
}