package com.mk.domain.useCase

import com.mk.domain.models.RanobeModel

interface IRanobePageTextRepository {
    suspend fun fetchRanobePageText(url: String): MutableList<String>?

}