package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel

interface IReturnRanobePageUseCase {
    suspend fun execute(url: String): RanobeModel?
}