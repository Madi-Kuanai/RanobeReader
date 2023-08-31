package com.mk.ranobereader.presentation.readingScreen


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.domain.models.FullRanobeModel
import com.mk.domain.models.PreviouslyReadRanobeModel
import com.mk.ranobereader.R
import com.mk.ranobereader.databinding.ActivityReadingScreenBinding
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModel
import com.mk.ranobereader.presentation.ranobeInfoScreen.viewModel.RanobeInfoViewModelFactory
import com.mk.ranobereader.presentation.readingScreen.viewModel.ReadingViewModel
import com.mk.ranobereader.presentation.readingScreen.viewModel.ReadingViewModelFactory
import kotlin.math.abs
import kotlin.math.roundToInt


@Suppress("KotlinConstantConditions")
class ReadingScreen : AppCompatActivity() {
    private var isExiting: Boolean = false
    lateinit var binding: ActivityReadingScreenBinding
    private lateinit var ranobeVM: RanobeInfoViewModel
    private lateinit var readVM: ReadingViewModel
    private lateinit var ranobe: FullRanobeModel
    private lateinit var currentChapterTitle: String
    private lateinit var currentChapterLink: String
    lateinit var textView: TextView
    var fullText = ""
    var isVisibleAppAndBottom = false
    private var touchStartX: Float = 0f
    private var touchStartY: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isExiting) {
            isExiting = false
        }

        binding = ActivityReadingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContent()
        createViewModel()
        setSupportActionBar(binding.toolbar)
        checkIsNewRanobe()
        readVM.getPageText(Const.BASE_URI + currentChapterLink)
        setSupportActionBar()
        binding.titleTv.text = currentChapterTitle
        setObservers()
        createBottomSheet()
        setSearchView()
        setSlider()
        binding.fabButton.setOnClickListener {
            binding.scrollview.post(Runnable {
                binding.scrollview.smoothScrollTo(
                    0,
                    binding.scrollview.getChildAt(0).height
                )
            })
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setContent() {
        textView = TextView(this).apply {
            setTextIsSelectable(true)
            id = View.generateViewId()
        }

        textView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchStartX = event.x
                    touchStartY = event.y
                    true
                }

                MotionEvent.ACTION_UP -> {
                    val deltaX = event.x - touchStartX
                    val deltaY = event.y - touchStartY
                    val swipeThreshold = 100f
                    val screenWidth = resources.displayMetrics.widthPixels
                    val sectorWidth = screenWidth / 3f

                    if (abs(deltaX) < swipeThreshold && abs(deltaY) < swipeThreshold) {
                        val clickArea = when {
                            event.x < sectorWidth -> -1
                            event.x < 2 * sectorWidth -> 0
                            else -> 1
                        }

                        if (clickArea == -1) {
                            Log.d(TAG, "setContent: left")
                            //Left side

                        } else if (clickArea == 1) {
                            Log.d(TAG, "setContent: right")
                            //Right side
                        } else if (clickArea == 0) {
                            if (event.action == MotionEvent.ACTION_UP) {
                                showAndHideAppAndBottom()
                            }
                        }
                    }

                    false
                }

                else -> false
            }
            true
        }
    }


    private fun showAndHideAppAndBottom(): Boolean {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        if (isVisibleAppAndBottom) {
            binding.appBarLayout.startAnimation(fadeInAnimation)
            binding.sheet.startAnimation(fadeInAnimation)
            binding.appBarLayout.visibility = View.VISIBLE
            binding.sheet.visibility = View.VISIBLE
        } else {
            binding.appBarLayout.startAnimation(fadeOutAnimation)
            binding.sheet.startAnimation(fadeOutAnimation)
            binding.appBarLayout.visibility = View.GONE
            binding.sheet.visibility = View.GONE
        }

        isVisibleAppAndBottom = !isVisibleAppAndBottom
        return true
    }

    private fun setSlider() {
        readVM.fontSize.observe(this) { font ->
            if (font in -1..100) {

                binding.fontSlider.value = font.toFloat()
                textView.textSize = font.toFloat()
            }
        }
        readVM.brightness.observe(this) { brightness ->
            binding.brightness.value = (brightness.toInt() / 0.01 / 255).toInt().toFloat()
            setBrightness(brightness)

        }
        binding.fontSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                textView.textSize = slider.value.toInt().toFloat()
                readVM.setFontSize(slider.value.toInt())

            }
        })

        binding.brightness.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                readVM.setBrightness((slider.value.toInt() * 0.01 * 255).roundToInt())
                setBrightness((slider.value.toInt() * 0.01 * 255).roundToInt())
            }
        })
    }

    private fun setSearchView() {
        binding.searchArea.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.searchArea.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                val inputMethodManager =
                    this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.searchArea.windowToken, 0)
                performSearch()
//                BottomSheetBehavior.from(binding.sheet).apply {
//                    state = BottomSheetBehavior.STATE_COLLAPSED
//                }
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun performSearch() {
        val searchText: String = binding.searchArea.text.toString().trim()
        val fullText: String = textView.text.toString()

        textView.setText(fullText, TextView.BufferType.SPANNABLE)
        if (searchText.isNotEmpty()) {
            val spannableText = textView.text as Spannable
            var index = fullText.indexOf(searchText, 0)
            while (index >= 0) {
                val endIndex = index + searchText.length
                spannableText.setSpan(
                    BackgroundColorSpan(Color.parseColor("#B3C6F4")),
                    index,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                index = fullText.indexOf(searchText, endIndex)
            }
        }

    }

    private fun createBottomSheet() {
        BottomSheetBehavior.from(binding.sheet).apply {
            this.peekHeight = 150
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setObservers() {
        readVM.content.observe(this) {
            fullText = ""
            binding.contentLayout.addView(textView)

            val myView = findViewById<View>(R.id.contentLayout)
//
//            textView.setOnTouchListener { _, event ->
//                BottomSheetBehavior.from(binding.sheet).state = BottomSheetBehavior.STATE_COLLAPSED
//                gestureDetector.onTouchEvent(event)
//            }
            it?.forEach { elem ->
                if (elem.contains(Const.IMG_TAG)) {
                    if (elem.replace(Const.IMG_TAG, "").isNotEmpty()) {
                        val imageView = ImageView(this)
                        val url: String = if (elem.contains("http")) {
                            elem.replace(Const.IMG_TAG, "")
                        } else {
                            (Const.BASE_URI + elem).replace(Const.IMG_TAG, "")
                        }
                        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .transform(RoundedCorners(20)).placeholder(R.drawable.black_image)
                            .into(imageView)
                        imageView.layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        imageView.id = View.generateViewId()
                        binding.contentLayout.addView(imageView)
                    }

                } else {
                    if (elem.isNotEmpty()) fullText += elem + "\n"
                }
                textView.text = fullText
            }

        }

    }

    private fun setSupportActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayUseLogoEnabled(false)
            setDisplayShowCustomEnabled(false)
            customView = null
        }

    }

    private fun checkIsNewRanobe() {
        if (intent.getBooleanExtra(Const.IS_NEW_RANOBE, true)) {
            if (intent.getSerializableExtra(Const.EXTRA_TYPE) as? FullRanobeModel != null) {
                ranobe = intent.getSerializableExtra(Const.EXTRA_TYPE) as FullRanobeModel

                currentChapterTitle = ranobe.chapters.keys.first()
                currentChapterLink = ranobe.chapters[currentChapterTitle].toString()
            }
        } else {
            if (intent.getSerializableExtra(Const.EXTRA_TYPE) as? FullRanobeModel != null) {
                val previouslyReadRanobeModel =
                    ranobeVM.getViewedRanobe(intent.getSerializableExtra(Const.EXTRA_TYPE) as FullRanobeModel) as PreviouslyReadRanobeModel
                currentChapterTitle = previouslyReadRanobeModel.lastChapterTitle
                currentChapterLink = previouslyReadRanobeModel.lastChapterLink
                val lastPosition = previouslyReadRanobeModel.lastPosition.toFloatOrNull()
                if (lastPosition != null) {
                    Log.d(TAG, "onCreate: not Null")
                    binding.scrollview.post(Runnable {
                        binding.scrollview.scrollTo(
                            0, lastPosition.toInt()
                        )
                    })

                }
                ranobe = previouslyReadRanobeModel
                Log.d(TAG, "Last Pos: ${previouslyReadRanobeModel.lastPosition.toFloat()}")
                Log.d(TAG, "Scroll Pos: ${binding.scrollview.scrollY.toFloat()}")

            }
        }
    }

    private fun createViewModel() {
        readVM = ViewModelProvider(
            this, ReadingViewModelFactory(application)
        )[ReadingViewModel::class.java]

        ranobeVM = ViewModelProvider(
            this, RanobeInfoViewModelFactory(
                application
            )
        )[RanobeInfoViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent()
                isExiting = false
                intent.putExtra(Const.RANOBE_MODEL, ranobe)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        if (this::ranobe.isInitialized) {
            Log.d(TAG, "onDestroy: ${binding.scrollview.scrollY}")
            val ranobe = PreviouslyReadRanobeModel(
                fullRanobeModel = ranobe,
                currentChapterTitle,
                currentChapterLink,
                binding.scrollview.scrollY.toString()
            )
            ranobeVM.setViewedRanobe(
                ranobe
            )
            if (isExiting) {
                Log.d(TAG, "onDestroy: isExiting")
                readVM.saveLastOpenedRanobe(
                    ranobe
                )
            } else {
                Log.d(TAG, "removeLast")
                readVM.removeLastOpenedRanobe()
            }
        }
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onDestroy() {

        Log.d(TAG, "onDestroy: $isExiting")
        super.onDestroy()
    }

    override fun onUserLeaveHint() {
        Log.d(TAG, "onUserLeaveHint")
        isExiting = true
        super.onUserLeaveHint()

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed")
        isExiting = false
        finish()
        super.onBackPressed()
    }

    private fun setBrightness(brightness: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(applicationContext)) {
                // Изменение яркости для Android 6.0 и выше
                Settings.System.putInt(
                    contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness
                )

                // Применение изменений
                val layoutParams = window.attributes
                layoutParams.screenBrightness = brightness / 255f
                window.attributes = layoutParams
            } else {
                // Перенаправление пользователя на экран настроек для разрешения изменения яркости
                val intent: Intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        } else {
            // Изменение яркости для Android ниже 6.0

            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)

            // Применение изменений
            val layoutParams = window.attributes
            layoutParams.screenBrightness = brightness / 255f
            window.attributes = layoutParams
        }
    }

}
