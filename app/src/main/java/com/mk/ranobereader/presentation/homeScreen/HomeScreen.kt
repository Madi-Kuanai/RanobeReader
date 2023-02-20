package com.mk.ranobereader.presentation.homeScreen

import android.annotation.SuppressLint
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
import com.mk.ranobereader.presentation.homeScreen.viewModel.HomeViewModel
import com.mk.ranobereader.presentation.homeScreen.viewModel.HomeViewModelFactory
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.FragmentHomeScreenBinding
import com.mk.domain.models.RanobeModel

class HomeScreen : Fragment() {
    lateinit var binding: FragmentHomeScreenBinding
    private lateinit var homeVM: HomeViewModel
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
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            homeVM.mvLst.observe(viewLifecycleOwner) { ranobes ->
                ranobes!!.forEach { ranobe ->
                    createViewAndAddToLinear(ranobe)
                }
            }
            homeVM.mvLayoutPos.observe(viewLifecycleOwner) {
                binding.rootScroller.y = it
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeVM.mvLayoutPos.value = binding.rootScroller.y
    }

    @SuppressLint("CutPasteId")
    private fun createViewAndAddToLinear(ranobeModel: RanobeModel) {

        val genresAdapter = GenresAdapter()
        val view = layoutInflater.inflate(
            R.layout.ranobe_card_with_description, binding.root,
            false
        )

        val linearLayoutManager = LinearLayoutManager(context)
        (view!!.findViewById<View>(R.id.title) as TextView).text = ranobeModel.name
        (view.findViewById<View>(R.id.description) as TextView).text = ranobeModel.description
        (view.findViewById<View>(R.id.imageView) as ImageView).minimumHeight =
            ViewGroup.LayoutParams.WRAP_CONTENT
        (view.findViewById<View>(R.id.imageView) as ImageView).minimumWidth =
            ViewGroup.LayoutParams.WRAP_CONTENT
        Glide.with(view)
            .load(ranobeModel.imageLink)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.black_image)
            .into((view.findViewById<View>(R.id.imageView) as ImageView))

        val recView = view.findViewById<RecyclerView>(R.id.genresRecView)
        recView.addItemDecoration(MarginItemDecoration(16))
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recView.layoutManager = linearLayoutManager
        recView.adapter = genresAdapter
        genresAdapter.addGenres(ranobeModel.genres)
        binding.mostLikedLayout.addView(view)
    }

}