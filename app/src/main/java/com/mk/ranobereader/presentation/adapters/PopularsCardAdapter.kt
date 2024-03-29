package com.mk.ranobereader.presentation.adapters

import android.annotation.SuppressLint
import android.content.Intent
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
import com.mk.domain.models.RanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.PopularsCardBinding
import com.mk.ranobereader.presentation.ranobeInfoScreen.RanobeInfoScreen

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
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, RanobeInfoScreen()::class.java)
                intent.putExtra(Const.RANOBE_MODEL, ranobeModel)
                binding.root.context.startActivity(intent)
            }
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

    fun clearCards() {
        // Clear the existing card list
        // For example, if you have a list named "cards", you can do:
        listOfPopularRanobe.clear()
        notifyDataSetChanged()
    }

}