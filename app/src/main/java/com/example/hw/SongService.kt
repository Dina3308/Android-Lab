package com.example.hw

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Binder
import android.os.IBinder

class SongService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val songBinder = SongBinder()
    private var actionPlaying : ActionPlaying? = null

    override fun onBind(intent: Intent): IBinder {
        return songBinder
    }

    inner class SongBinder : Binder() {
        fun getService() : SongService {
            return this@SongService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val ACTION_NEXT = "NEXT"
        val ACTION_PREV = "PREVIOUS"
        val ACTION_PLAY = "PLAY"
        val ACTION_STOP = "STOP"

        when (intent?.getStringExtra("actionName")) {
            ACTION_NEXT -> actionPlaying?.nextBtnClicked()
            ACTION_PREV -> actionPlaying?.prevBtnClicked()
            ACTION_PLAY -> actionPlaying?.playPauseBtnClicked()
            ACTION_STOP -> actionPlaying?.stopBtnClicked()

        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun create(audio: Int?) {
        mediaPlayer = audio?.let { MediaPlayer.create(applicationContext, it) }
    }

    fun start(){
        mediaPlayer?.start()
    }

    fun playMedia(audio: Int?){
        if(mediaPlayer != null){
            stop()
            release()
        }
        create(audio)
        start()
    }

    fun pause(){
        mediaPlayer?.pause()
    }
    fun stop(){
        mediaPlayer?.stop()
    }

    fun isPlaying() : Boolean? {
        return mediaPlayer?.isPlaying
    }

    fun release(){
        mediaPlayer?.release()
    }

    fun getDuration() : Int{
        return mediaPlayer?.duration ?: 0
    }
    fun getCurrentPosition() : Int?{
        return mediaPlayer?.currentPosition
    }
    fun seekTo(position: Int){
        mediaPlayer?.seekTo(position)
    }

    fun onCompeted() {
        mediaPlayer?.setOnCompletionListener{
            actionPlaying?.nextBtnClicked()
        }
    }

    fun setCallBack(actionPlaying: ActionPlaying){
        this.actionPlaying = actionPlaying
    }
}
