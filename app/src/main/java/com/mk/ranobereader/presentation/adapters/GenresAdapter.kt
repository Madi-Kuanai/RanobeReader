package com.mk.ranobereader.presentation.homeScreen

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mk.domain.Const
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.GenresCardBinding

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.GenresHolder>() {
    private var genresMap = mapOf<String, String>()
    private var genresKeysList = listOf<String>()

    class GenresHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: GenresCardBinding = GenresCardBinding.bind(itemView)
        fun bind(genreName: String, genreHref: String) {
            binding.genreText.text = genreName
            binding.root.setOnClickListener {
                Log.d(Const.TAG, genreHref)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresHolder {
        return GenresHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.genres_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenresHolder, position: Int) {
        holder.bind(genresKeysList[position], genresMap[genresKeysList[position]]!!)
    }

    override fun getItemCount(): Int {
        return genresKeysList.size
    }

    fun addGenres(genre: Map<String, String>) {
        genresKeysList = genre.keys.toList()
        genresMap = genre
    }
}

class MarginItemDecoration(
    private val top: Int,
    private val bottom: Int,
    private val left: Int,
    private val right: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            top = top
            right = right
            bottom = bottom
            left = left

        }
    }
}
