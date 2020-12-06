package com.example.hw.activity

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hw.*
import com.example.hw.notification.PlayerNotification
import com.example.hw.service.SongService
import kotlinx.android.synthetic.main.activity_content.*

class PlayerActivity : AppCompatActivity(){

    private var songService: SongAidlInterface? = null
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when(intent?.action){
                Actions.NEXT.name -> {
                    initViews(songService?.currentSong)
                    R.drawable.pause
                     Log.e("next", "error")
                }
                Actions.PREVIOUS.name -> {
                    initViews(songService?.currentSong)
                    R.drawable.pause
                }
                Actions.PLAY.name -> {
                    playStop.setImageResource(if(intent.getStringExtra("icon") == "play")
                        R.drawable.play  else R.drawable.pause)
                }
                Actions.STOP.name -> {
                    initializeSeekBar()
                    playStop.setImageResource(R.drawable.play)
                }
            }
        }
    }
    private val myConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            songService = SongAidlInterface.Stub.asInterface(service)
            if (intent.getStringExtra("action") != null){
                playStop.setImageResource(if(songService?.isPlaying == true) R.drawable.pause
                    else R.drawable.play)
            }
            initViews(songService?.currentSong)
            initializeSeekBar()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            songService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val song = intent.getParcelableExtra<Song>("song")
        ContextCompat.startForegroundService(this, Intent(this, SongService::class.java).putExtra("song", song))
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

        stop.setOnClickListener {
            stopBtnClicked()
        }

        previous.setOnClickListener {
            prevBtnClicked()
        }

        next.setOnClickListener {
            nextBtnClicked()
        }

        playStop.setOnClickListener {
            playPauseBtnClicked()
        }
    }


    private fun initViews(song: Song?){
        song?.cover?.let { cover.setImageResource(it) }
        name.text = song?.name
        singer.text = song?.singer
        timeEnd.text = song?.duration
    }

    private fun initializeSeekBar() {
        seekBar.max = (songService?.duration ?: 0) / 1000
        runnable = Runnable {
            val position = songService?.currentPosition?.div(1000)
            if (position != null) {
                seekBar.progress = position
                timeStart.text = formattedTime(position)
            }

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun formattedTime(position: Int) : String {
        val second = (position % 60).toString()
        val minute = (position / 60).toString()
        val totalOut = "$minute:$second"
        val totalNew = "$minute:0$second"
        return if (second.length == 1) totalNew else totalOut
    }


    private fun playPauseBtnClicked() {
        if (songService?.isPlaying == true) {
            playStop.setImageResource(R.drawable.play)
            songService?.pause()

        } else {
            playStop.setImageResource(R.drawable.pause)
            songService?.play()
        }
        initializeSeekBar()
    }

    private fun nextBtnClicked() {
        songService?.next()
        playStop.setImageResource(R.drawable.pause)
        initViews(songService?.currentSong)
        initializeSeekBar()
    }

    private fun prevBtnClicked() {
        songService?.next()
        playStop.setImageResource(R.drawable.pause)
        initViews(songService?.currentSong)
        initializeSeekBar()
    }

    private fun stopBtnClicked() {
        songService?.stop()
        playStop.setImageResource(R.drawable.play)
        initializeSeekBar()
        playStop.setImageResource(R.drawable.play)
    }


    override fun onStart() {
        super.onStart()
        Intent(this, SongService::class.java).also { intent ->
            bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }

        registerReceiver(broadCastReceiver, IntentFilter().also {
            it.addAction(Actions.NEXT.name)
            it.addAction(Actions.PREVIOUS.name)
            it.addAction(Actions.PLAY.name)
            it.addAction(Actions.STOP.name)
        })
    }

    override fun onStop() {
        super.onStop()
        unbindService(myConnection)
        unregisterReceiver(broadCastReceiver)
    }
    
}


