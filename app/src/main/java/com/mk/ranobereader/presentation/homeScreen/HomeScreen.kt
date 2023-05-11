package com.mk.ranobereader.presentation.homeScreen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mk.domain.Const
import com.mk.domain.Const.EXTRA_TYPE
import com.mk.domain.Const.MOST_POPULAR_TYPE
import com.mk.domain.Const.MOST_VIEWED_TYPE
import com.mk.domain.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.FragmentHomeScreenBinding
import com.mk.ranobereader.presentation.adapters.PopularsCardAdapter
import com.mk.ranobereader.presentation.adapters.UpdateCardAdapter
import com.mk.ranobereader.presentation.homeScreen.viewModel.HomeViewModel
import com.mk.ranobereader.presentation.homeScreen.viewModel.HomeViewModelFactory
import com.mk.ranobereader.presentation.ranobeInfoScreen.RanobeInfoScreen
import com.mk.ranobereader.presentation.ranobeListScreen.RanobeListScreen

class HomeScreen : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var homeVM: HomeViewModel
    val popularsCardAdapter = PopularsCardAdapter()
    val updatesCardAdapter = UpdateCardAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        Log.d(TAG, "Home Create")
        homeVM = ViewModelProvider(
            this.requireActivity(),
            HomeViewModelFactory(context?.applicationContext as Application)
        )[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        val linear = LinearLayoutManager(context)
        linear.orientation = LinearLayoutManager.HORIZONTAL
        binding.popularsRecView.layoutManager = linear
        binding.popularsRecView.adapter = popularsCardAdapter
        val gridLayout = GridLayoutManager(context, 2)
        gridLayout.orientation = GridLayoutManager.HORIZONTAL
        binding.updatesRecView.layoutManager = gridLayout
        binding.updatesRecView.adapter = updatesCardAdapter
        try {
            homeVM.mvWithDescription.observe(viewLifecycleOwner) { ranobes ->
                ranobes?.slice(0..6)?.forEach { ranobe ->
                    createViewAndAddToLinear(ranobe)
                }
            }
            homeVM.mvMostViewedLayoutPosition.observe(viewLifecycleOwner) {
                binding.rootScroller.y = it
            }
            homeVM.mvPopularsPosition.observe(viewLifecycleOwner) {
                binding.popularsRecView.x = it
            }
            homeVM.mvUpdatesPosition.observe(viewLifecycleOwner) {
                binding.updatesRecView.scrollToPosition(it.toInt())
            }
            homeVM.mvPopulars.observe(viewLifecycleOwner) { ranobes ->
                ranobes?.forEach { ranobeModel ->
                    popularsCardAdapter.addCard(ranobeModel)
                }
            }
            homeVM.mvUpdates.observe(viewLifecycleOwner) { ranobes ->
                ranobes.forEach { updatedRanobeModel ->
                    updatesCardAdapter.addCard(updatedRanobeModel)
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, e.toString())

        }
        binding.allPopulars.setOnClickListener(openMostPopularList())
        binding.allMostViewed.setOnClickListener(openMostViewedList())

        binding.pullToRefresh.setOnRefreshListener { refreshData() }

    }


    private fun refreshData() {
        binding.popularsRecView.removeAllViews()
        binding.mostLikedLayout.removeAllViews()
        homeVM.refreshData()
        binding.pullToRefresh.isRefreshing = false
    }

    private fun openMostViewedList(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(this.requireActivity(), RanobeListScreen()::class.java)
            intent.putExtra(EXTRA_TYPE, MOST_VIEWED_TYPE)
            startActivity(intent)
        }
    }

    private fun openMostPopularList(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(this.requireActivity(), RanobeListScreen()::class.java)
            intent.putExtra(EXTRA_TYPE, MOST_POPULAR_TYPE)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        homeVM.mvMostViewedLayoutPosition.value = binding.rootScroller.y

    }


    @SuppressLint("CutPasteId")
    private fun createViewAndAddToLinear(ranobeModel: RanobeModel) {
        val genresAdapter = GenresAdapter()
        val view = layoutInflater.inflate(
            R.layout.ranobe_card_with_description, binding.root,
            false
        )

        val linearLayoutManager = LinearLayoutManager(context)
        (view!!.findViewById<View>(R.id.title) as TextView).text = ranobeModel.title
        (view.findViewById<View>(R.id.description) as TextView).text = ranobeModel.description
        (view.findViewById<View>(R.id.imageCover) as ImageView).minimumHeight =
            ViewGroup.LayoutParams.WRAP_CONTENT
        (view.findViewById<View>(R.id.imageCover) as ImageView).minimumWidth =
            ViewGroup.LayoutParams.WRAP_CONTENT
        Glide.with(view)
            .load(ranobeModel.imageLink)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.black_image)
            .into((view.findViewById<View>(R.id.imageCover) as ImageView))
        val recView = view.findViewById<RecyclerView>(R.id.genresRecView)
        recView.addItemDecoration(MarginItemDecoration(top = 2, left = 5, right = 5, bottom = 5))
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recView.layoutManager = linearLayoutManager
        recView.adapter = genresAdapter
        genresAdapter.addGenres(ranobeModel.genres)
        view.setOnClickListener {
            Log.d(TAG, "Click")
            val intent = Intent(
                view.context,
                RanobeInfoScreen()::class.java
            )
            intent.putExtra(Const.RANOBE_MODEL, ranobeModel)
            binding.root.context.startActivity(intent)
        }
        binding.mostLikedLayout.addView(view)
    }

}
/*class CheckNetworkConnection (private val connectivityManager: ConnectivityManager) : LiveData<Boolean>() {

    constructor(application: Application) : this(application.getSystemService(Context.CONNECTIVITY_SERVICE) as  ConnectivityManager)
    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(),networkCallback)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}*/