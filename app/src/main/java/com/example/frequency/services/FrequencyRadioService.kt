package com.example.frequency.services

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.frequency.foundation.services.BaseService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class FrequencyRadioService @Inject constructor(): BaseService() {

    private val binder by lazy { StationBinder() }
    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()


    }

    fun runAction(ms: MusicState){

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)


    }

    override fun onDestroy() {
        super.onDestroy()


    }

    fun initializeMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, null)

    }

    fun startTranslation(){
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    fun getNameOfSong(): String =
        resources.getResourceEntryName(5275)
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ENGLISH)
                else it.toString()
            }.replace("_", " ")

    inner class StationBinder : Binder() {
        fun getService(): FrequencyRadioService = this@FrequencyRadioService
    }

    companion object{

    }
}