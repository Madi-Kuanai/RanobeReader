package com.mk.ranobereader.presentation.ranobeInfoScreen

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.domain.Const
import com.mk.domain.models.IRanobe
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.ActivityRanobeInfoScreenBinding


class RanobeInfoScreen() : AppCompatActivity() {

    lateinit var binding: ActivityRanobeInfoScreenBinding
    lateinit var ranobeModel: IRanobe
    var isImageFitToScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRanobeInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ranobeModel = intent.getSerializableExtra(Const.RANOBE_MODEL) as IRanobe
        initToolBar()
        binding.coverImage.setOnClickListener {
            if (isImageFitToScreen) {
                isImageFitToScreen = false
                binding.coverImage.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                binding.coverImage.adjustViewBounds = true
            } else {
                isImageFitToScreen = true
                binding.coverImage.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                binding.coverImage.scaleType = ImageView.ScaleType.FIT_XY
            }

        }
    }


    private fun initToolBar() {
        binding.toolbar.title = ""
        binding.title.text = ranobeModel.title
        binding.description.text = ranobeModel.description

        Glide.with(binding.root).load(ranobeModel.imageLink)
            .apply(RequestOptions().override(500, 500))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.black_image)
            .into(binding.coverImage)
        setSupportActionBar(binding.toolbar)
    }
}