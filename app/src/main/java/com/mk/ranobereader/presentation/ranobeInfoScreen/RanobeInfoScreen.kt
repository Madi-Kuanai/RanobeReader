package com.mk.ranobereader.presentation.ranobeInfoScreen


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.IRanobe
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.ActivityRanobeInfoScreenBinding
import com.mk.ranobereader.presentation.adapters.GenresAdapter
import com.mk.ranobereader.presentation.adapters.MarginItemDecoration
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
    private var isExpand: Boolean = true
    private lateinit var fullRanobeModel: FullRanobeModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRanobeInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainLayout.visibility = GONE
        ranobeModel = intent.getSerializableExtra(Const.RANOBE_MODEL) as IRanobe
        ranobeVM = ViewModelProvider(
            this, RanobeInfoViewModelFactory(
                this.application
            )
        )[RanobeInfoViewModel::class.java]
        ranobeVM.getData(ranobeModel.linkToRanobe)
        genresAdapter = GenresAdapter()
        tagsAdapter = GenresAdapter()
        setObserves()
        setBottomButtons()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
            setDisplayUseLogoEnabled(false)
            setDisplayShowCustomEnabled(false)
            setBackgroundDrawable(ColorDrawable(0x00000000))
            customView = null
        }
    }

    private fun setBottomButtons() {
        binding.readButton.setOnClickListener {
            val intent = Intent(this, ReadingScreen::class.java)
            intent.putExtra(Const.IS_NEW_RANOBE, true)
            intent.putExtra(Const.EXTRA_TYPE, fullRanobeModel)
            startActivity(intent)
        }
        binding.continueButton.setOnClickListener {
            val intent = Intent(this, ReadingScreen::class.java)
            intent.putExtra(Const.IS_NEW_RANOBE, false)
            intent.putExtra(Const.EXTRA_TYPE, fullRanobeModel)
            startActivity(intent)
        }
    }

    private fun setObserves() {
        try {
            ranobeVM.ranobe.observe(this) { ranobe ->
                binding.mainLayout.visibility = VISIBLE
                fullRanobeModel = ranobe
                setFullData(ranobe)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setFullData(nRanobeMode: FullRanobeModel) {
        Log.d(TAG, "setFullData: ${nRanobeMode.imageLink}")
        val url = URL(nRanobeMode.imageLink)
        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())

        Glide.with(binding.root).load(image).apply(RequestOptions().override(700, 600))
            .diskCacheStrategy(DiskCacheStrategy.ALL).transform(RoundedCorners(20))
            .placeholder(R.drawable.black_image).into(binding.coverImage)
        val drawable: Drawable = BitmapDrawable(resources, image)
        binding.a.background = drawable

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
//            supportActionBar?.apply {
//                setHomeAsUpIndicator(ColorDrawable(dominantColor))
//            }
        }


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
                    top = 2, bottom = 5, left = 10
                )
            )
            genresRecView.layoutManager =
                LinearLayoutManager(this@RanobeInfoScreen, LinearLayoutManager.HORIZONTAL, false)
            genresRecView.adapter = genresAdapter

            tagsAdapter.addGenres(nRanobeMode.tags)
            tags.addItemDecoration(
                MarginItemDecoration(
                    top = 2, bottom = 5, left = 10
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
            binding.continueButton.visibility = VISIBLE
        } else {
            binding.continueButton.visibility = GONE
        }

//        binding.coverImage.setOnClickListener {
//            try {
//                val intent = Intent(this, FullScreenImageActivity::class.java)
//                val bitmap = Bitmap.createBitmap(
//                    drawable.intrinsicWidth,
//                    drawable.intrinsicHeight,
//                    Bitmap.Config.ARGB_8888
//                )
//                val canvas = Canvas(bitmap)
//                drawable.setBounds(0, 0, canvas.width, canvas.height)
//                drawable.draw(canvas)
//                intent.putExtra(Const.IMAGE_RES, bitmap)
//                startActivity(intent)
//            } catch (e: Exception) {
//                Log.d(TAG, "setFullData: $e")
//            }
//        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) ranobeModel =
            intent.getSerializableExtra(Const.RANOBE_MODEL) as IRanobe
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun getContrastColor(color: Int): Int {
        val luminance = ColorUtils.calculateLuminance(color)

        return if (luminance > 0.5) {
            Color.BLACK
        } else {
            Color.WHITE
        }
    }

    @ColorInt
    fun darkenColor(@ColorInt color: Int): Int {
        return Color.HSVToColor(FloatArray(3).apply {
            Color.colorToHSV(color, this)
            this[2] *= 0.8f
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}