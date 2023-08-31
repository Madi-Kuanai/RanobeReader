package com.mk.ranobereader.presentation.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.domain.models.RanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.PopularsCardBinding
import com.mk.ranobereader.databinding.UpdateCardBinding
import com.mk.ranobereader.presentation.ranobeInfoScreen.RanobeInfoScreen

class ViewedRanobeCardAdapter :
    RecyclerView.Adapter<ViewedRanobeCardAdapter.ViewedCardsViewHolder>() {
    private var listOfViewedRanobe: MutableList<PreviouslyReadRanobeModel> = ArrayList()

    class ViewedCardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: UpdateCardBinding = UpdateCardBinding.bind(itemView)
        fun bind(ranobeModel: PreviouslyReadRanobeModel) {
            binding.title.text = ranobeModel.title
            Log.d(TAG, "ImageLink: " + ranobeModel.imageLink)
            Glide.with(binding.root).load(Const.BASE_URI + ranobeModel.imageLink)
                .apply(RequestOptions().override(250, 500))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).transform(RoundedCorners(20))
                .into(binding.imageCover)
            binding.titleOfLastUpdate.text = "Читать с ${ranobeModel.lastChapterTitle}"
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, RanobeInfoScreen()::class.java)
                intent.putExtra(Const.RANOBE_MODEL, ranobeModel)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewedCardsViewHolder {
        val viewItem =
            LayoutInflater.from(parent.context).inflate(R.layout.update_card, parent, false)
        return ViewedCardsViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewedCardsViewHolder, position: Int) {
        holder.bind(listOfViewedRanobe[position])
    }

    override fun getItemCount(): Int {
        return listOfViewedRanobe.size
    }

    @SuppressLint("NotifyDataSetChanged")
    internal fun addCard(ranobeModel: PreviouslyReadRanobeModel) {
        listOfViewedRanobe.add(ranobeModel)
        notifyDataSetChanged()
    }

    fun clearCards() {
        listOfViewedRanobe.clear()
        notifyDataSetChanged()
    }

}