package com.mk.ranobereader.presentation.fullScreenImageScreen

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.mk.domain.Const
import com.mk.domain.Const.TAG
import com.mk.ranobereader.R

class FullScreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val bitmap = intent.getParcelableExtra<Bitmap>(Const.IMAGE_RES)
        Log.d(TAG, "BITMAP: " + bitmap.toString())

        val fullScreenImageView: ImageView = findViewById(R.id.fullScreenImageView)
        fullScreenImageView.setImageBitmap(bitmap)
    }

}
