package com.example.exotest.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.example.exotest.R
import com.google.android.exoplayer2.ui.PlayerView

class CustomPlayerView(
    context: Context,
    attrs: AttributeSet? = null
) : PlayerView(context, attrs) {
    val artView: ImageView = findViewById<ImageView>(R.id.exo_artwork).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
    }
}