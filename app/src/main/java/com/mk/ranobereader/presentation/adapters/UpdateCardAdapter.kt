package com.mk.ranobereader.presentation.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.domain.Const.TAG

import com.mk.domain.models.UpdatedRanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.UpdateCardBinding

class UpdateCardAdapter : RecyclerView.Adapter<UpdateCardAdapter.UpdatesCardViewHolder>() {
    var listOfPopularRanobe: MutableList<UpdatedRanobeModel> = ArrayList()

    class UpdatesCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: UpdateCardBinding = UpdateCardBinding.bind(itemView)
        fun bind(ranobeModel: UpdatedRanobeModel) {
            binding.title.text = ranobeModel.title
            binding.dateOfAdded.text = ranobeModel.dateOfUpdate
            binding.titleOfLastUpdate.text = ranobeModel.titleOfLastUpdate
//            Log.d(TAG, ranobeModel.dateOfUpdate)
//            Log.d(TAG, ranobeModel.titleOfLastUpdate)
//            Log.d(TAG, "--------------------")

            Glide.with(binding.imageCover).load(ranobeModel.imageLink)
                .apply(RequestOptions().override(125, 125))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.black_image)
                .into(binding.imageCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdatesCardViewHolder {
        val viewItem =
            LayoutInflater.from(parent.context).inflate(R.layout.update_card, parent, false)
        return UpdatesCardViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: UpdatesCardViewHolder, position: Int) {
        holder.bind(listOfPopularRanobe[position])
    }

    override fun getItemCount(): Int {
        return listOfPopularRanobe.size
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun addCard(ranobeModel: UpdatedRanobeModel) {
        listOfPopularRanobe.add(ranobeModel)
        notifyDataSetChanged()
    }
}