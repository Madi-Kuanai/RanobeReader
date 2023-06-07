package com.mk.ranobereader.presentation.ranobeInfoScreen


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.TranslateAnimation
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.data.repositories.SharedPref.ViewedRanobePreferenceService
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.IRanobe
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.ActivityRanobeInfoScreenBinding
import com.mk.ranobereader.presentation.homeScreen.GenresAdapter
import com.mk.ranobereader.presentation.homeScreen.MarginItemDecoration
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModel
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModelFactory
import com.mk.ranobereader.presentation.readingScreen.ReadingScreen
import java.net.URL


class RanobeInfoScreen() : AppCompatActivity() {

    lateinit var binding: ActivityRanobeInfoScreenBinding
    lateinit var ranobeModel: IRanobe
    private lateinit var ranobeVM: RanobeInfoViewModel
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var tagsAdapter: GenresAdapter
    private var isExpand: Boolean = false
    private lateinit var fullRanobeModel: FullRanobeModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRanobeInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainLayout.visibility = GONE
        ranobeModel = intent.getSerializableExtra(Const.RANOBE_MODEL) as IRanobe
        val shared = ViewedRanobePreferenceService(
            getSharedPreferences(
                Const.VIEWED_SHARED_KEY, MODE_PRIVATE
            )
        )
        ranobeVM = ViewModelProvider(
            this, RanobeInfoViewModelFactory(
                this.application
            )
        )[RanobeInfoViewModel::class.java]
        ranobeVM.getData(ranobeModel.linkToRanobe)
        genresAdapter = GenresAdapter()
        tagsAdapter = GenresAdapter()
        setObserves()
        binding.readButton.setOnClickListener {
            val intent = Intent(this, ReadingScreen::class.java)
            intent.putExtra(Const.EXTRA_TYPE, fullRanobeModel)
            startActivity(intent)
        }
    }

    private fun setObserves() {
        try {
            ranobeVM.ranobe.observe(this) { ranobe ->
                binding.mainLayout.visibility = View.VISIBLE
                fullRanobeModel = ranobe
                setFullData(ranobe)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setFullData(nRanobeMode: FullRanobeModel) {
        val url = URL(nRanobeMode.imageLink)
        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())

        Glide.with(binding.root).load(image).apply(RequestOptions().override(700, 600))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).transform(RoundedCorners(20))
            .placeholder(R.drawable.black_image).into(binding.coverImage)
        Palette.from(image).generate {

            val dominantColor: Int = it?.getDominantColor(0) ?: 1
            val contrastColor = getContrastColor(dominantColor)
            binding.imageBack.setBackgroundColor(dominantColor)
            binding.imageBack.background.alpha = 210
            binding.mainLayout.setBackgroundColor(dominantColor)
            val drawable = binding.infoLayout.background as GradientDrawable
            drawable.setColor(darkenColor(dominantColor))// Replace with your desired color
            binding.infoLayout.background = drawable
            binding.textView2.setTextColor(contrastColor)
            binding.textView3.setTextColor(contrastColor)
            binding.textView4.setTextColor(contrastColor)
            binding.rating.setTextColor(contrastColor)
            binding.ratingOfTranslate.setTextColor(contrastColor)
            binding.likes.setTextColor(contrastColor)
        }

        val drawable: Drawable = BitmapDrawable(resources, image)
        binding.a.background = drawable
        binding.description.text = nRanobeMode.description
        if (nRanobeMode.author.isNullOrEmpty()) {
            binding.author.visibility = GONE
        } else {
            binding.author.text = nRanobeMode.author
        }
        binding.apply {
            title.text = nRanobeMode.title
            description.text = nRanobeMode.description
            stateOfTitle.text = nRanobeMode.statusOfTitle
            stateOfTranslationTextView.text = nRanobeMode.stateOfTranslation
            numberOfChaptersTextView.text = nRanobeMode.numberOfChapters
            yearOfAnons.text = nRanobeMode.yearOfAnons
            rating.text = "${nRanobeMode.rating[0]} / 5"
            ratingOfTranslate.text = "${nRanobeMode.ratingOfTranslate[0]} / 5"
            likes.text = nRanobeMode.likes.toString()

            genresAdapter.addGenres(nRanobeMode.genres)
            genresRecView.addItemDecoration(
                MarginItemDecoration(
                    top = 2, left = 10, right = 5, bottom = 5
                )
            )
            genresRecView.layoutManager =
                LinearLayoutManager(this@RanobeInfoScreen, LinearLayoutManager.HORIZONTAL, false)
            genresRecView.adapter = genresAdapter

            tagsAdapter.addGenres(nRanobeMode.tags)
            tags.addItemDecoration(
                MarginItemDecoration(
                    top = 2, left = 10, right = 5, bottom = 5
                )
            )
            tags.layoutManager =
                LinearLayoutManager(this@RanobeInfoScreen, LinearLayoutManager.HORIZONTAL, false)
            tags.adapter = tagsAdapter
        }
        val initialHeight = binding.expandedInfo.height

        binding.expandBtn.setOnClickListener {
            if (isExpand) {
                // Collapse the container
                binding.expandIcon.setImageResource(R.mipmap.ic_expand_less)
                val animate = TranslateAnimation(0f, 0f, 0f, initialHeight.toFloat())

                // Duration of animation
                animate.duration = 250
                animate.fillAfter = true
                binding.expandedInfo.startAnimation(animate)
                binding.expandedInfo.visibility = GONE
            } else {
                // Expand the container
                binding.expandIcon.setImageResource(R.mipmap.ic_expand_more)
                binding.expandedInfo.visibility = VISIBLE
                val animate = TranslateAnimation(0f, 0f, initialHeight.toFloat(), 0f)

                // Duration of animation
                animate.duration = 500
                animate.fillAfter = false
                binding.expandedInfo.startAnimation(animate)
            }
            isExpand = !isExpand
        }
        if (ranobeVM.isViewedRanobe(fullRanobeModel)) {
            Log.d(TAG, "viewed")
            binding.continueButton.visibility = VISIBLE
        }

    }

    private fun getContrastColor(color: Int): Int {
        // Calculate the luminance of the color
        val luminance = ColorUtils.calculateLuminance(color)

        // Determine the contrast color based on the luminance
        return if (luminance > 0.5) {
            Color.BLACK // Black for lighter colors
        } else {
            Color.WHITE // White for darker colors
        }
    }

    @ColorInt
    fun darkenColor(@ColorInt color: Int): Int {
        return Color.HSVToColor(FloatArray(3).apply {
            Color.colorToHSV(color, this)
            this[2] *= 0.8f
        })
    }
}