package com.example.exotest

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import java.io.File

// The Cache size of our app is 512MB
private const val VIDEO_CACHE_SIZE = 512L * 1024 * 1024

class CirqleApplication : Application() {
    private lateinit var userAgent: String
    private lateinit var videoCache: SimpleCache
    private lateinit var databaseProvider: ExoDatabaseProvider
    private lateinit var cacheFile: File

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     *
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     *
     *
     * If you override this method, be sure to call `super.onCreate()`.
     *
     *
     * Be aware that direct boot may also affect callback order on
     * Android [android.os.Build.VERSION_CODES.N] and later devices.
     * Until the user unlocks the device, only direct boot aware components are
     * allowed to run. You should consider that all direct boot unaware
     * components, including such [android.content.ContentProvider], are
     * disabled until user unlock happens, especially when component callback
     * order matters.
     */
    override fun onCreate() {
        super.onCreate()

        // Initialize our Variables
        databaseProvider = ExoDatabaseProvider(this)
        cacheFile = getExternalFilesDir(null) ?: filesDir
        userAgent = Util.getUserAgent(this, "Cirqle")
        // Create our Video Cache
        videoCache = SimpleCache(
            cacheFile,
            LeastRecentlyUsedCacheEvictor(VIDEO_CACHE_SIZE),
            databaseProvider
        )
    }

    fun getVideoCache(): SimpleCache = videoCache

    fun getDatabaseProvider(): ExoDatabaseProvider = databaseProvider

    /**
     * Returns a DataSource.Factory that reads from videoCache
     */
    fun buildDataSourceFactory(): CacheDataSourceFactory = CacheDataSourceFactory(
        videoCache,
        DefaultDataSourceFactory(this, DefaultHttpDataSourceFactory(userAgent)),
        FileDataSourceFactory(),
        CacheDataSinkFactory(videoCache, DEFAULT_BUFFER_SIZE.toLong()),
        CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
        null
    )

    fun releaseCache() {
        videoCache.release()
    }
}