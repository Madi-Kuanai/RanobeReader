package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel

interface IRanobePageTextUseCase {
    suspend fun execute(url: String): MutableList<String>?

}