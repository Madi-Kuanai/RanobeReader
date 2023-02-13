package com.mk.ranobereader.HomeScreen

import com.mk.ranobereader.models.RanobeModel
import androidx.recyclerview.widget.RecyclerView
import com.mk.ranobereader.HomeScreen.RanobeWithDescriptionCardAdapter.RanobeWithDescriptionHolder
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

//            Picasso.get().load(ranobeModel.getImageLink()).into(binding.imageView, new Callback() {
//                @Override
//                public void onSuccess() {
//                    System.out.println("Success");
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    System.out.println("Error in picasso" + e.getMessage());
//
//                }
//            });
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