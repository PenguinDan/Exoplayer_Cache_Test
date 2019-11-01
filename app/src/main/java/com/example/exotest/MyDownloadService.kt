package com.example.exotest

import android.app.Notification
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import java.lang.UnsupportedOperationException

class MyDownloadService : DownloadService(FOREGROUND_NOTIFICATION_ID_NONE) {
    /**
     * Returns a [DownloadManager] to be used to downloaded content. Called only once in the
     * life cycle of the process.
     */
    override fun getDownloadManager(): DownloadManager =
        DownloadManager(
            this,
            getApp().getDatabaseProvider(),
            getApp().getVideoCache(),
            getApp().buildDataSourceFactory()
        ).apply { maxParallelDownloads = 5 }

    /**
     * Returns a notification to be displayed when this service running in the foreground. This method
     * is called when there is a download state change and periodically while there are active
     * downloads. The periodic update interval can be set using [.DownloadService].
     *
     *
     * On API level 26 and above, this method may also be called just before the service stops,
     * with an empty `downloads` array. The returned notification is used to satisfy system
     * requirements for foreground services.
     *
     *
     * Download services that do not wish to run in the foreground should be created by setting the
     * `foregroundNotificationId` constructor argument to [ ][.FOREGROUND_NOTIFICATION_ID_NONE]. This method will not be called in this case, meaning it can
     * be implemented to throw [UnsupportedOperationException].
     *
     * @param downloads The current downloads.
     * @return The foreground notification to display.
     */
    override fun getForegroundNotification(downloads: MutableList<Download>?): Notification =
        throw UnsupportedOperationException()

    /**
     * Returns a [Scheduler] to restart the service when requirements allowing downloads to take
     * place are met. If `null`, the service will only be restarted if the process is still in
     * memory when the requirements are met.
     */
    override fun getScheduler(): Scheduler? = PlatformScheduler(this, 1)

    private fun getApp(): CirqleApplication = application as CirqleApplication
}