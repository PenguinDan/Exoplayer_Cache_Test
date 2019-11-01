package com.example.exotest

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.cache.CacheKeyFactory
import com.google.android.exoplayer2.upstream.cache.CacheUtil
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

private const val SAMPLE_1 = "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8"
private const val SAMPLE_2 = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"

class MainActivity : AppCompatActivity() {
    private var videoPlayer: SimpleExoPlayer? = null
    private var playerView: PlayerView? = null
    private var mediaSource1: MediaSource? = null
    private var mediaSource2: MediaSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.mainPlayerView)
        precache(Uri.parse(SAMPLE_1), 1)
        precache(Uri.parse(SAMPLE_2), 2)

        findViewById<Button>(R.id.mainVideo1).setOnClickListener {
            Toast.makeText(this, "Video1", Toast.LENGTH_SHORT).show()
            videoPlayer?.prepare(mediaSource1)
        }
        findViewById<Button>(R.id.mainVideo2).setOnClickListener {
            Toast.makeText(this, "Video2", Toast.LENGTH_SHORT).show()
            videoPlayer?.prepare(mediaSource2)
        }
    }

    override fun onStart() {
        super.onStart()
        videoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        playerView?.player = videoPlayer
        videoPlayer?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()

        videoPlayer?.release()
        videoPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        playerView = null
        (application as CirqleApplication).releaseCache()
    }

    private fun getApp(): CirqleApplication = application as CirqleApplication

    private fun precache(uri: Uri, sourceInt: Int) {
        DownloadHelper.forHls(
            uri, getApp().buildDataSourceFactory(), DefaultRenderersFactory(this)
        ).prepare(object : DownloadHelper.Callback {
            /**
             * Called when preparation completes.
             *
             * @param helper The reporting [DownloadHelper].
             */
            override fun onPrepared(helper: DownloadHelper?) {
                val downloadRequest = helper?.getDownloadRequest(Util.getUtf8Bytes(uri.toString()))
                if (downloadRequest != null) {
                    DownloadService.sendAddDownload(
                        this@MainActivity,
                        MyDownloadService::class.java,
                        downloadRequest,
                        false
                    )

                    if (sourceInt == 1) {
                        mediaSource1 = DownloadHelper.createMediaSource(
                            downloadRequest,
                            getApp().buildDataSourceFactory()
                        )
                    } else {
                        mediaSource2 = DownloadHelper.createMediaSource(
                            downloadRequest,
                            getApp().buildDataSourceFactory()
                        )
                    }
                }
                helper?.release()
            }

            /**
             * Called when preparation fails.
             *
             * @param helper The reporting [DownloadHelper].
             * @param e The error.
             */
            override fun onPrepareError(helper: DownloadHelper?, e: IOException?) {

            }
        })
    }
}
