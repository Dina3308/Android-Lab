package com.example.hw.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.hw.*
import com.example.hw.notification.PlayerNotification

class SongService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val songs: ArrayList<Song> = SongRepository.getSongs()
    private  var song: Song? = null
    private val mBinder: SongAidlInterface.Stub = object : SongAidlInterface.Stub(){

        override fun play() {
            this@SongService.start()
            PlayerNotification.createNotification(
                applicationContext,
                song,
                R.drawable.pause
            )
        }

        override fun stop() {
            this@SongService.stop()
            this@SongService.release()
            this@SongService.create(song)
            PlayerNotification.createNotification(
                applicationContext,
                song,
                R.drawable.play
            )
        }

        override fun pause() {
            this@SongService.pause()
            PlayerNotification.createNotification(
                applicationContext,
                song,
                R.drawable.play
            )
        }

        override fun isPlaying(): Boolean = this@SongService.isPlaying()

        override fun playMedia() {
            this@SongService.playMedia(song)
        }

        override fun seekTo(position: Int) {
            this@SongService.seekTo(position)
        }

        override fun getCurrentPosition(): Int = this@SongService.getCurrentPosition()

        override fun getDuration(): Int = this@SongService.getDuration()

        override fun next() {
            this@SongService.song = songs[if (songs.indexOf(song) == songs.size - 1) 0 else songs.indexOf(
                song
            ) + 1]
            PlayerNotification.createNotification(applicationContext, song, R.drawable.pause)
            playMedia(song)
        }


        override fun prev(){
            this@SongService.song = songs[if (songs.indexOf(song) == 0) songs.size - 1 else songs.indexOf(
                song
            ) - 1]
            PlayerNotification.createNotification(applicationContext, song, R.drawable.pause)
            playMedia(song)
        }

        override fun getCurrentSong(): Song? = song


    }
    override fun onBind(intent: Intent): IBinder = mBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val song1 = intent?.getParcelableExtra<Song>("song")
        if(song1 != null){
            song = song1
            playMedia(song)
            startForeground(
                5, PlayerNotification.createNotification(
                    applicationContext, song, R.drawable.pause
                )
            )
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
            mediaPlayer = MediaPlayer.create(applicationContext, it.audio)
            this.song = it

            mediaPlayer?.setOnCompletionListener{
                nextBroadcastReceiver()
            }
        }

    }

    private fun start(){
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

    private fun pause(){
        mediaPlayer?.pause()
    }
    private fun stop(){
        mediaPlayer?.stop()
    }

    private fun isPlaying() : Boolean = mediaPlayer?.isPlaying ?: false

    private fun release(){
        mediaPlayer?.release()
    }

    private fun getDuration() : Int =  mediaPlayer?.duration ?: 0

    private fun getCurrentPosition() : Int = mediaPlayer?.currentPosition ?: 0

    private fun seekTo(position: Int){
        mediaPlayer?.seekTo(position)
    }

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
        if(isPlaying()){
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

    override fun onDestroy() {
        super.onDestroy()
        stopBroadcastReceiver()
        stopForeground(true)
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).cancel(5)

    }
}
