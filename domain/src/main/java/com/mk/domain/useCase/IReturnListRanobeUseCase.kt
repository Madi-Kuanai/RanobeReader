package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel

interface IReturnListRanobeUseCase {
    suspend fun execute(page: Int): List<RanobeModel>
}