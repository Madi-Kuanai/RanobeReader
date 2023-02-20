package com.mk.ranobereader.presentation.homeScreen.viewModel.adapters

import com.mk.domain.models.RanobeModel
import androidx.recyclerview.widget.RecyclerView
import com.mk.ranobereader.presentation.homeScreen.viewModel.adapters.RanobeWithDescriptionCardAdapter.RanobeWithDescriptionHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.mk.ranobereader.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.mk.ranobereader.databinding.RanobeCardWithDescriptionBinding
import java.util.*

class RanobeWithDescriptionCardAdapter(context: Context, listOfRanobeWithDescription: MutableList<RanobeModel>) : RecyclerView.Adapter<RanobeWithDescriptionHolder>() {
    var listOfRanobeWithDescription: MutableList<RanobeModel> = ArrayList()
    var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RanobeWithDescriptionHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.ranobe_card_with_description, parent, false)
        return RanobeWithDescriptionHolder(viewItem)
    }

    override fun onBindViewHolder(holder: RanobeWithDescriptionHolder, position: Int) {
        holder.bind(listOfRanobeWithDescription[position])
    }

    override fun getItemCount(): Int {
        return listOfRanobeWithDescription.size
    }

    class RanobeWithDescriptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RanobeCardWithDescriptionBinding = RanobeCardWithDescriptionBinding.bind(itemView)
        fun bind(ranobeModel: RanobeModel) {
            binding.title.text = ranobeModel.name
            binding.description.text = ranobeModel.description
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRanobe(ranobeModels: List<RanobeModel>?) {
        listOfRanobeWithDescription.addAll(ranobeModels!!)
        notifyDataSetChanged()
    }

    init {
        this.listOfRanobeWithDescription = listOfRanobeWithDescription
        this.context = context
    }
}