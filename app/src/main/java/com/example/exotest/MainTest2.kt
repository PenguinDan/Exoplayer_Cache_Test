package com.example.exotest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.Coil
import coil.api.load
import com.example.exotest.custom.CustomPlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer

private const val FOOD_IMAGE_1 =
    "https://media.glamour.com/photos/5b046883c5e2f62a895ae90a/master/w_721,h_447,c_limit/Screen%20Shot%202018-05-22%20at%202.58.39%20PM.png"

class MainTest2 : AppCompatActivity() {
    private var playerView: CustomPlayerView? = null
    private var mediaPlayer: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_test_2)

        // Get a reference to the player view
        playerView = findViewById(R.id.mainTest2Root)
        Coil.load(this, FOOD_IMAGE_1) {
            target {
                Log.d("Test", "Loaded image")
                playerView?.artView?.setImageDrawable(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = ExoPlayerFactory.newSimpleInstance(this)
        playerView?.player = mediaPlayer
    }

    override fun onStop() {
        super.onStop()
        playerView?.player = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}