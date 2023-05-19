package com.mk.ranobereader.presentation.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.domain.Const
import com.mk.domain.models.RanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.RanobeCardWithDescriptionBinding
import com.mk.ranobereader.presentation.adapters.RanobeWithDescriptionCardAdapter.RanobeWithDescriptionHolder
import com.mk.ranobereader.presentation.homeScreen.GenresAdapter
import com.mk.ranobereader.presentation.homeScreen.MarginItemDecoration
import com.mk.ranobereader.presentation.ranobeInfoScreen.RanobeInfoScreen

class RanobeWithDescriptionCardAdapter() : RecyclerView.Adapter<RanobeWithDescriptionHolder>() {
    var listOfRanobeWithDescription: MutableList<RanobeModel> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RanobeWithDescriptionHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.ranobe_card_with_description, parent, false)
        return RanobeWithDescriptionHolder(viewItem)
    }

    override fun onBindViewHolder(holder: RanobeWithDescriptionHolder, position: Int) {
        holder.bind(listOfRanobeWithDescription[position])
    }

    override fun getItemCount(): Int {
        return listOfRanobeWithDescription.size
    }

    class RanobeWithDescriptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RanobeCardWithDescriptionBinding =
            RanobeCardWithDescriptionBinding.bind(itemView)

        fun bind(ranobeModel: RanobeModel) {
            binding.title.text = ranobeModel.title
            binding.description.text = ranobeModel.description
            Glide.with(binding.imageCover).load(ranobeModel.imageLink)
                .apply(RequestOptions().override(250, 500))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.black_image)
                .into(binding.imageCover)
            val genresAdapter = GenresAdapter()
            val linearLayoutManager = LinearLayoutManager(binding.root.context)

            binding.genresRecView.addItemDecoration(
                MarginItemDecoration(
                    top = 2,
                    left = 10,
                    right = 10,
                    bottom = 5
                )
            )
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            binding.genresRecView.layoutManager = linearLayoutManager
            genresAdapter.addGenres(ranobeModel.genres)
            binding.genresRecView.adapter = genresAdapter
            binding.root.setOnClickListener {
                Log.d(Const.TAG, "Click")
                val intent = Intent(
                    binding.root.context,
                    RanobeInfoScreen()::class.java
                )
                intent.putExtra(Const.RANOBE_MODEL, ranobeModel)
                binding.root.context.startActivity(intent)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRanobe(ranobeModels: List<RanobeModel>?) {
        listOfRanobeWithDescription.addAll(ranobeModels!!)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRanobe(ranobeModel: RanobeModel) {
        listOfRanobeWithDescription.add(ranobeModel)
        notifyDataSetChanged()
    }
}