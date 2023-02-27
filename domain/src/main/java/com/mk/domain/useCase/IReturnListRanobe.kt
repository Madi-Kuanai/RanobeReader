package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel

interface IReturnListRanobe {
    suspend fun execute(): List<RanobeModel>
}