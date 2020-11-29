package com.example.hw

import android.app.NotificationManager
import android.content.*
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_content.*

class PlayerActivity : AppCompatActivity(), ActionPlaying {

    private var songService: SongService? = null
    private lateinit var song: Song
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private lateinit var playThread: Thread
    private lateinit var nextThread: Thread
    private lateinit var prevThread: Thread
    private lateinit var stopThread: Thread
    private val songs: ArrayList<Song> = SongRepository.getSongs()
    private val myConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            songService = (service as SongService.SongBinder).getService()
            songService?.setCallBack(this@PlayerActivity)
            createSong()
            PlayerNotification.createNotification(applicationContext, song.cover, song.singer, song.name, R.drawable.pause )

        }

        override fun onServiceDisconnected(className: ComponentName) {
            songService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        Intent(this, SongService::class.java).also { intent ->
            bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }
        song = intent.getParcelableExtra("song")
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    songService?.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        initViews(song)

    }


    private fun initViews(song: Song){
        PlayerNotification.createNotification(applicationContext, song.cover, song.singer, song.name, R.drawable.pause)
        cover.setImageResource(song.cover)
        name.text = song.name
        singer.text = song.singer
        timeEnd.text = song.duration
    }

    private fun createSong() {
        songService?.create(song.audio)
        songService?.start()
        play.setImageResource(R.drawable.pause)
        initializeSeekBar()
    }

    private fun initializeSeekBar() {
        seekBar.max = (songService?.getDuration() ?: 0) / 1000
        runnable = Runnable {
            val position = songService?.getCurrentPosition()?.div(1000)
            if (position != null) {
                seekBar.progress = position
                timeStart.text = formattedTime(position)
            }

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        songService?.onCompeted()
    }

    private fun formattedTime(position: Int) : String {
        val second = (position % 60).toString()
        val minute = (position / 60).toString()
        val totalOut = "$minute:$second"
        val totalNew = "$minute:0$second"
        return if (second.length == 1) totalNew else totalOut
    }

    private fun playThreadBtn() {
        playThread = Thread {
            play.setOnClickListener {
                playPauseBtnClicked()
            }
        }
        playThread.start()
    }

    private fun nextThreadBtn() {
        nextThread = Thread {
            next.setOnClickListener {
                nextBtnClicked()
            }
        }
       nextThread.start()
    }

    private fun prevThreadBtn() {
        prevThread = Thread {
            previous.setOnClickListener {
                prevBtnClicked()
            }
        }
        prevThread.start()
    }

    private fun stopThreadBtn() {
        stopThread = Thread {
            stop.setOnClickListener {
                stopBtnClicked()
            }
        }
        prevThread.start()
    }
    override fun prevBtnClicked() {
        song = songs[if (songs.indexOf(song) < 1) songs.size - 1 else songs.indexOf(song) - 1]
        songService?.playMedia(song.audio)
        initViews(song)
        play.setImageResource(R.drawable.pause)
        initializeSeekBar()
    }

    override fun nextBtnClicked(){
        song = songs[if (songs.indexOf(song) == songs.size - 1) 0 else songs.indexOf(song) + 1]
        songService?.playMedia(song.audio)
        initViews(song)
        play.setImageResource(R.drawable.pause)
        initializeSeekBar()
    }

    override fun playPauseBtnClicked() {

        if(songService?.isPlaying() == true){
            play.setImageResource(R.drawable.play)
            songService?.pause()
            PlayerNotification.createNotification(applicationContext, song.cover, song.singer, song.name, R.drawable.play )

        }
        else{
            play.setImageResource(R.drawable.pause)
            songService?.start()
            PlayerNotification.createNotification(applicationContext, song.cover, song.singer, song.name, R.drawable.pause )
        }
        initializeSeekBar()
    }

    override fun stopBtnClicked() {
        songService?.stop()
        songService?.release()
        songService?.create(song.audio)
        initializeSeekBar()
        play.setImageResource(R.drawable.play)
        PlayerNotification.createNotification(applicationContext, song.cover, song.singer, song.name, R.drawable.play)
    }

    override fun onStart() {
        super.onStart()
        playThreadBtn()
        nextThreadBtn()
        prevThreadBtn()
        stopThreadBtn()
    }

    override fun onDestroy() {
        super.onDestroy()
        songService?.stop()
        unbindService(myConnection)
        stopService(Intent(this, SongService::class.java))
        PlayerNotification.createNotification(applicationContext, song.cover, song.singer, song.name, R.drawable.pause).cancelAll()
    }

}


