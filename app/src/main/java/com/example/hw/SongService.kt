package com.example.hw

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.create
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.activity_content.*

class SongService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val songBinder = SongBinder()
    private  var song: Song? = null
    private val songs: ArrayList<Song> = SongRepository.getSongs()
    override fun onBind(intent: Intent): IBinder {
        return songBinder
    }

    inner class SongBinder : Binder() {
        fun getService() : SongService {
            return this@SongService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val song1 = intent?.getParcelableExtra<Song>("song")
        if(song1 != null){
            song = song1
            playMedia(song)
            startForeground(5, PlayerNotification.createNotification(
                applicationContext, song, R.drawable.pause))
        }
        if(intent?.action != null){
            when (intent.action) {
                Actions.NEXT.name -> nextBroadcastReceiver()
                Actions.PREVIOUS.name -> prevBroadcastReceiver()
                Actions.PLAY.name -> playBroadcastReceiver()
                Actions.STOP.name -> stopBroadcastReceiver()
            }
        }


        return START_STICKY
    }

    fun create(song: Song?) {
        song?.let {
            mediaPlayer = create(applicationContext, it.audio)
            this.song = it

            mediaPlayer?.setOnCompletionListener{
                Log.e("end", "end")
                nextBroadcastReceiver()
            }
        }

    }

    fun start(){
        mediaPlayer?.start()
    }

    fun playMedia(song: Song?){
        if(mediaPlayer != null){
            stop()
            release()
        }
        song?.let {
            create(song)
            this.song = it
            start()
        }
    }

    fun pause(){
        mediaPlayer?.pause()
    }
    fun stop(){
        mediaPlayer?.stop()
    }

    fun isPlaying() : Boolean?{
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

    fun getCurrentSong(): Song? = song

    private fun nextBroadcastReceiver(){
        song = songs[if (songs.indexOf(song) == songs.size - 1) 0 else songs.indexOf(song) + 1]
        playMedia(song)
        PlayerNotification.createNotification(applicationContext, song, R.drawable.pause)
        sendBroadcast(Intent(Actions.NEXT.name))
    }

    private fun prevBroadcastReceiver(){
        song = songs[if (songs.indexOf(song) == 0) songs.size - 1 else songs.indexOf(song) - 1]
        playMedia(song)
        PlayerNotification.createNotification(applicationContext, song, R.drawable.pause)
        sendBroadcast(Intent(Actions.PREVIOUS.name))
    }

    private fun stopBroadcastReceiver(){
        stop()
        release()
        create(song)
        PlayerNotification.createNotification(applicationContext, song, R.drawable.play)
        sendBroadcast(Intent(Actions.STOP.name))
    }

    private fun playBroadcastReceiver(){
        val intent = Intent(Actions.PLAY.name)
        if(isPlaying() == true){
            pause()
            PlayerNotification.createNotification(
                applicationContext,
                song,
                R.drawable.play
            )
            intent.putExtra("icon", "play")

        }
        else {
            start()
            PlayerNotification.createNotification(
                applicationContext,
                song,
                R.drawable.pause
            )
            intent.putExtra("icon", "pause")
        }
        sendBroadcast(intent)
    }


}
