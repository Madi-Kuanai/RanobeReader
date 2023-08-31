package com.mk.domain.useCase

class LoadRanobePageTextUseCase(
    private val iRanobePageTextRepository: IRanobePageTextRepository
) : IRanobePageTextUseCase {
    override suspend fun execute(url: String): MutableList<String>? {
        return iRanobePageTextRepository.fetchRanobePageText(url)
    }
}