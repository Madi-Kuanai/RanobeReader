package com.mk.ranobereader.presentation.ranobeListScreen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mk.core.Const.METHOD
import com.mk.core.Const.TAG
import com.mk.data.repositories.ranobes.RanobeRepositoryImpl
import com.mk.domain.Const
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.IReturnListRanobe
import com.mk.domain.useCase.LoadMostPopularsUseCase
import com.mk.domain.useCase.LoadMostViewedUseCase
import com.mk.ranobereader.databinding.ActivityRanobeListScreenBinding
import com.mk.ranobereader.presentation.adapters.RanobeWithDescriptionCardAdapter

class RanobeListScreen : AppCompatActivity() {
    lateinit var binding: ActivityRanobeListScreenBinding
    lateinit var listScreenVM: RanobeListViewModel
    val descriptionCardAdapter = RanobeWithDescriptionCardAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRanobeListScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        Log.d(TAG, "START")
        super.onStart()
        val iReturnListRanobe: IReturnListRanobe = if (intent.getStringExtra(METHOD) == Const.GET) {
            LoadMostPopularsUseCase(RanobeRepositoryImpl())
        } else {
            LoadMostViewedUseCase(RanobeRepositoryImpl())
        }
        binding.goBackSign.setOnClickListener { finish() }
        listScreenVM = ViewModelProvider(
            this,
            RanobeListViewModelFactory(iReturnListRanobe)
        )[RanobeListViewModel::class.java]
        listScreenVM.listRanobe.observe(this, Observer { ranobes ->
            Log.d(TAG, "OBSERVE")
            ranobes.let {
                it.forEach { ranobeModel ->
                    addRanobe(ranobeModel)
                }
            }
        })
    }

    private fun addRanobe(ranobeModel: RanobeModel) {
        val linearLayoutManager = LinearLayoutManager(this)
        descriptionCardAdapter.addRanobe(ranobeModel)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.listRanobeRec.layoutManager = linearLayoutManager
        binding.listRanobeRec.adapter = descriptionCardAdapter
    }

}