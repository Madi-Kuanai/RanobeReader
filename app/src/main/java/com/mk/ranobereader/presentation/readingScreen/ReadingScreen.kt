package com.mk.ranobereader.presentation.readingScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mk.domain.Const
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.ranobereader.databinding.ActivityReadingScreenBinding
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModel
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModelFactory

class ReadingScreen : AppCompatActivity() {
    lateinit var binding: ActivityReadingScreenBinding
    private lateinit var ranobeVM: RanobeInfoViewModel
    private lateinit var ranobe: FullRanobeModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ranobeVM = ViewModelProvider(
            this, RanobeInfoViewModelFactory(
                application
            )
        )[RanobeInfoViewModel::class.java]
        if (intent.getSerializableExtra(Const.EXTRA_TYPE) as? FullRanobeModel != null) {
            ranobe = intent.getSerializableExtra(Const.EXTRA_TYPE) as FullRanobeModel
        }

    }

    fun loadChapters() {

    }

    override fun onDestroy() {
        //TODO  сделай каст с фулл ту превиус и закинь в 43 строку
        ranobeVM.setViewedRanobe(
            PreviouslyReadRanobeModel(
                fullRanobeModel = ranobe,
                "",
                binding.scrollview.y.toString()
            )
        )
        super.onDestroy()
    }
}