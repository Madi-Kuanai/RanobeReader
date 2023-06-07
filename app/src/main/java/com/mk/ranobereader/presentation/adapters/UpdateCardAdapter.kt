package com.mk.ranobereader.presentation.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.domain.Const
import com.mk.domain.models.IRanobe
import com.mk.domain.models.UpdatedRanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.UpdateCardBinding
import com.mk.ranobereader.presentation.ranobeInfoScreen.RanobeInfoScreen

class UpdateCardAdapter : RecyclerView.Adapter<UpdateCardAdapter.UpdatesCardViewHolder>() {
    var listOfPopularRanobe: MutableList<UpdatedRanobeModel> = ArrayList()

    class UpdatesCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: UpdateCardBinding = UpdateCardBinding.bind(itemView)
        fun bind(ranobeModel: UpdatedRanobeModel) {
            binding.title.text = ranobeModel.title
            binding.dateOfAdded.text = ranobeModel.dateOfUpdate
            binding.titleOfLastUpdate.text = ranobeModel.titleOfLastUpdate

            Glide.with(binding.imageCover).load(ranobeModel.imageLink)
                .apply(RequestOptions().override(100, 100))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.black_image)
                .into(binding.imageCover)
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, RanobeInfoScreen::class.java)
                intent.putExtra(Const.RANOBE_MODEL, ranobeModel as IRanobe)
                binding.root.context.startActivity(intent)
            }
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

    fun clearCards() {
        // Clear the existing card list
        // For example, if you have a list named "cards", you can do:
        listOfPopularRanobe.clear()
        notifyDataSetChanged()
    }

}