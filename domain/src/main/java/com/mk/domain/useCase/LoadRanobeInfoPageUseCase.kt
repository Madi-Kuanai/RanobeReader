package com.mk.domain.useCase

import com.mk.domain.models.FullRanobeModel

class LoadRanobeInfoPageUseCase(
    private val IReturnRanobe: IReturnRanobeRepository
) : IReturnRanobePageUseCase {
    override suspend fun execute(url: String): FullRanobeModel {
        return IReturnRanobe.fetchRanobe(url)
    }

}