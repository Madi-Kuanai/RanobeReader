package com.mk.data.repositories.ranobes

import android.content.SharedPreferences
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.IRanobeFromSharedPrefRepository

class RanobeFromSharedPrefRepositoryImpl : IRanobeFromSharedPrefRepository {


    override suspend fun fetchRanobeList(): MutableList<RanobeModel> {
        TODO("Not yet implemented")
    }


}