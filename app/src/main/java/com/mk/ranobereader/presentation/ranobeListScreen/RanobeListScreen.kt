package com.mk.ranobereader.presentation.ranobeListScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mk.data.repositories.ranobes.RanobeRepositoryImpl
import com.mk.domain.Const.EXTRA_TYPE
import com.mk.domain.Const.MOST_POPULAR_TYPE
import com.mk.domain.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.domain.useCase.IReturnListRanobeUseCase
import com.mk.domain.useCase.LoadMostPopularsUseCase
import com.mk.domain.useCase.LoadMostViewedUseCase
import com.mk.ranobereader.databinding.ActivityRanobeListScreenBinding
import com.mk.ranobereader.presentation.adapters.RanobeWithDescriptionCardAdapter
import com.mk.ranobereader.presentation.homeScreen.MarginItemDecoration

class RanobeListScreen : AppCompatActivity() {
    lateinit var binding: ActivityRanobeListScreenBinding
    lateinit var listScreenVM: RanobeListViewModel
    val descriptionCardAdapter = RanobeWithDescriptionCardAdapter()
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var iReturnListRanobeUseCase: IReturnListRanobeUseCase
    val lstOfNameOfRanobeTitle: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRanobeListScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomEvent()
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.listRanobeRec.layoutManager = linearLayoutManager
        binding.listRanobeRec.adapter = descriptionCardAdapter

    }

    override fun onStart() {
        super.onStart()
        iReturnListRanobeUseCase = if (intent.getStringExtra(EXTRA_TYPE) == MOST_POPULAR_TYPE) {
            Log.d(TAG, "GET")
            LoadMostPopularsUseCase(RanobeRepositoryImpl())
        } else {
            Log.d(TAG, "POST")
            LoadMostViewedUseCase(RanobeRepositoryImpl())
        }

        binding.goBackSign.setOnClickListener { finish() }
        binding.pullToRefresh.setOnRefreshListener { refreshData() }
        descriptionCardAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        setViewModelObservers()
    }

    private fun refreshData() {
        binding.listRanobeRec.removeAllViews()
        listScreenVM.refreshData()
        binding.pullToRefresh.isRefreshing = false
    }

    private fun setViewModelObservers() {
        listScreenVM = ViewModelProvider(
            this,
            RanobeListViewModelFactory(iReturnListRanobeUseCase)
        )[RanobeListViewModel::class.java]

        listScreenVM.listRanobe.observe(this, Observer { ranobes ->
            ranobes.let {
                binding.progressBar.visibility = View.GONE
                it?.forEach { ranobeModel ->
                    if (ranobeModel.title !in lstOfNameOfRanobeTitle) {
                        addRanobe(ranobeModel)
                        lstOfNameOfRanobeTitle.add(ranobeModel.title)
                    }
                }
            }
        })
        listScreenVM.screenPos.observe(this) {
            binding.listRanobeRec.layoutManager?.scrollToPosition(it)
        }
    }

    private fun setBottomEvent() {
        binding.listRanobeRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!binding.listRanobeRec.canScrollVertically(1) && dy > 0) {
                    binding.progressBar.visibility = View.VISIBLE
                    listScreenVM.screenPos.postValue((binding.listRanobeRec.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
                    listScreenVM.pageOfList.postValue(listScreenVM.pageOfList.value?.plus(1))
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addRanobe(ranobeModel: RanobeModel) {
        descriptionCardAdapter.addRanobe(ranobeModel)
        binding.listRanobeRec.addItemDecoration(MarginItemDecoration(top = 2, left = 3, right = 3, bottom = 5))
        descriptionCardAdapter.notifyDataSetChanged()
    }

}