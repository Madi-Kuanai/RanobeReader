package com.mk.ranobereader.presentation.homeScreen.viewModel.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.domain.models.RanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.PopularsCardBinding

class PopularsCardAdapter : RecyclerView.Adapter<PopularsCardAdapter.PopularsCardViewHolder>() {
    var listOfPopularRanobe: MutableList<RanobeModel> = ArrayList()

    class PopularsCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: PopularsCardBinding = PopularsCardBinding.bind(itemView)
        fun bind(ranobeModel: RanobeModel) {
            itemView.findViewById<TextView>(R.id.titleWithContrast).text = ranobeModel.title
            itemView.findViewById<TextView>(R.id.titleWithContrast).textColors
            Glide.with(itemView).load(ranobeModel.imageLink)
                .apply(RequestOptions().override(250, 500))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.black_image)
                .into(binding.imageCover)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularsCardViewHolder {
        val viewItem =
            LayoutInflater.from(parent.context).inflate(R.layout.populars_card, parent, false)
        return PopularsCardViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: PopularsCardViewHolder, position: Int) {
        holder.bind(listOfPopularRanobe[position])
    }

    override fun getItemCount(): Int {
        return listOfPopularRanobe.size
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun addCard(ranobeModel: RanobeModel) {
        listOfPopularRanobe.add(ranobeModel)
        notifyDataSetChanged()
    }
}