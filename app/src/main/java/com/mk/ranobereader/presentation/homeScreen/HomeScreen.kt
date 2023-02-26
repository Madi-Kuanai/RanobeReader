package com.mk.ranobereader.presentation.homeScreen

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mk.core.Const.TAG
import com.mk.domain.models.RanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.FragmentHomeScreenBinding
import com.mk.ranobereader.presentation.homeScreen.viewModel.HomeViewModel
import com.mk.ranobereader.presentation.homeScreen.viewModel.HomeViewModelFactory
import com.mk.ranobereader.presentation.homeScreen.viewModel.adapters.PopularsCardAdapter
import com.mk.ranobereader.presentation.ranobeListScreen.RanobeListScreen

class HomeScreen : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var homeVM: HomeViewModel
    val popularsCardAdapter = PopularsCardAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        Log.d(TAG, "Home Create")
        homeVM = ViewModelProvider(
            this.requireActivity(),
            HomeViewModelFactory()
        )[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        try {
            homeVM.mvLst.observe(viewLifecycleOwner) { ranobes ->
                ranobes!!.forEach { ranobe ->
                    createViewAndAddToLinear(ranobe)
                }
            }
            homeVM.mvMostViewedLayoutPosition.observe(viewLifecycleOwner) {
                binding.rootScroller.y = it
            }
            homeVM.mvPopulars.observe(viewLifecycleOwner) { ranobes ->
                ranobes!!.forEach { ranobeModel ->
                    createViewAndAddToRecycler(ranobeModel)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        binding.allPopulars.setOnClickListener(openListScreen())
    }

    private fun openListScreen(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(this.requireActivity(), RanobeListScreen()::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeVM.mvMostViewedLayoutPosition.value = binding.rootScroller.y
    }

    private fun createViewAndAddToRecycler(ranobeModel: RanobeModel) {
        val linearLayoutManager = LinearLayoutManager(context)
        popularsCardAdapter.addCard(ranobeModel)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.popularsRecView.layoutManager = linearLayoutManager
        binding.popularsRecView.adapter = popularsCardAdapter
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
        recView.addItemDecoration(MarginItemDecoration(16))
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recView.layoutManager = linearLayoutManager
        recView.adapter = genresAdapter
        genresAdapter.addGenres(ranobeModel.genres)
        binding.mostLikedLayout.addView(view)
    }

}