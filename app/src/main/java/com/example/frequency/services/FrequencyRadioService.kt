package com.example.frequency.services

import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.frequency.foundation.services.BaseService
import com.example.frequency.network.coronet.ICronetEngineProvider
import com.example.frequency.network.radio_browser.models.Station
import com.example.frequency.screen.song.SongFragment.Companion.KEY_SER_STATION
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ext.cronet.CronetDataSource
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeoutException
import javax.inject.Inject


@AndroidEntryPoint
class FrequencyRadioService : BaseService() {

    @Inject
    lateinit var engineProvider: ICronetEngineProvider

    private val binder by lazy { StationBinder() }
    private lateinit var exoPlayer: ExoPlayer
    private var musicState = MusicState.STOP
    private var station: Station? = null
    private var stationBroadcastUri: String? = null

    init {
        Log.d(TAG, "Service starts.")
    }

    override fun onBind(intent: Intent): IBinder {
        station = intent.getParcelableExtra(KEY_SER_STATION)
        stationBroadcastUri = station?.url ?: station?.urlResolved

        initializeMediaPlayer()

        Log.d(TAG, "${station?.name}")
        Log.d(TAG, "${station?.country}")
        return binder
    }

    override fun onCreate() {
        super.onCreate()


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
        Log.d(TAG, "Service destroy.")
    }

    fun runAction(mState: MusicState) {
        musicState = mState
        when (mState) {
            MusicState.PLAY -> startMusic()
            MusicState.PAUSE -> pauseMusic()
            MusicState.STOP -> stopMusic()
        }
    }

    private fun startMusic() {
        if (!exoPlayer.isPlaying) {
            exoPlayer.play()
        }
    }

    private fun pauseMusic() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        }
    }

    private fun stopMusic() {
        if (exoPlayer.isPlaying) {
            exoPlayer.stop()
        }
    }

    private fun initializeMediaPlayer() {
        Log.d(TAG, "initPlayer")
        val cronetDataSourceFactory =
            CronetDataSource.Factory(engineProvider.getEngine(), Executors.newSingleThreadExecutor())
        val dataSourceFactory = DefaultDataSource.Factory(this, cronetDataSourceFactory)

        exoPlayer = ExoPlayer
            .Builder(this)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(dataSourceFactory)
                    .setLiveTargetOffsetMs(5000)
            )
            .build()

        setMediaItem()
        exoPlayer.prepare()
    }

    private fun setMediaItem() {
        try {
            val bUri = Uri.parse(stationBroadcastUri)
            if (stationBroadcastUri != null && bUri != Uri.EMPTY) {
                exoPlayer.setMediaItem(createMediaItem(bUri))
            }
            Log.d(TAG, "$stationBroadcastUri")

        } catch (e: TimeoutException) {
            Log.d(TAG, e.message.toString())
        } catch (e: IOException) {
            Log.d(TAG, e.message.toString())
        }

    }


    private fun createMediaItem(mediaUri: Uri): MediaItem {
        return MediaItem.Builder()
            .setUri(mediaUri)
            .build()
    }


    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class StationBinder : Binder() {
        fun getService(): FrequencyRadioService = this@FrequencyRadioService
    }

    companion object {

        @JvmStatic
        private val TAG = FrequencyRadioService::class.java.simpleName


    }
}