package com.mk.ranobereader.presentation.ranobeInfoScreen

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.IRanobe
import com.mk.domain.models.RanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.ActivityRanobeInfoScreenBinding
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModel
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModelFactory


class RanobeInfoScreen() : AppCompatActivity() {

    lateinit var binding: ActivityRanobeInfoScreenBinding
    lateinit var ranobeModel: IRanobe
    private lateinit var ranobeVM: RanobeInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRanobeInfoScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ranobeModel = intent.getSerializableExtra(Const.RANOBE_MODEL) as IRanobe
        ranobeVM =
            ViewModelProvider(
                this,
                RanobeInfoViewModelFactory()
            )[RanobeInfoViewModel::class.java]
        ranobeVM.setUrl(ranobeModel.linkToRanobe)
        setObserves()
        initToolBar()
    }

    private fun setObserves() {
        try {
            ranobeVM.ranobe.observe(this) { ranobe ->
                Log.d(TAG, ranobe.toString())
                setFullData(ranobe)
            }
        } catch (e: Exception) {

        }
    }

    private fun setFullData(nRanobeMode: FullRanobeModel) {
        Glide.with(binding.root)
            .load(nRanobeMode.imageLink)
            .apply(RequestOptions().override(500, 500))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.black_image)
            .into(binding.coverImage)
        binding.description.text = nRanobeMode.description
        if (nRanobeMode.author.isNullOrEmpty()) {
            binding.author.visibility = GONE
        } else {
            binding.author.text = nRanobeMode.author
        }
    }

    private fun initToolBar() {
        binding.toolbar.title = ""
        binding.title.text = ranobeModel.title
        binding.description.text = ranobeModel.description


        setSupportActionBar(binding.toolbar)
    }
}