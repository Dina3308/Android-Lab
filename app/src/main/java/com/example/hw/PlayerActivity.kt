package com.example.hw

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_content.*


class PlayerActivity : AppCompatActivity() {

    private var songService: SongService? = null
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private val songs: ArrayList<Song> = SongRepository.getSongs()
    private val myConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            songService = (service as SongService.SongBinder).getService()
            if (intent.getStringExtra("action") != null){
                initViews(songService?.getCurrentSong())
            }
            initializeSeekBar()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            songService = null
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                Actions.NEXT.name -> {
                    initViews(songService?.getCurrentSong())
                    R.drawable.pause
                }
                Actions.PREVIOUS.name -> {
                    initViews(songService?.getCurrentSong())
                    R.drawable.pause
                }
                Actions.PLAY.name -> {
                        play.setImageResource(if(intent.getStringExtra("icon") == "play")
                            R.drawable.play  else R.drawable.pause)
                }
                Actions.STOP.name -> {
                    initializeSeekBar()
                    play.setImageResource(R.drawable.play)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        var song = intent.getParcelableExtra<Song>("song")
        ContextCompat.startForegroundService(
            this, Intent(this, SongService::class.java).putExtra(
                "song",
                song
            )
        )

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


    private fun initViews(song: Song?){
        song?.cover?.let { cover.setImageResource(it) }
        name.text = song?.name
        singer.text = song?.singer
        timeEnd.text = song?.duration
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
    }

    private fun formattedTime(position: Int) : String {
        val second = (position % 60).toString()
        val minute = (position / 60).toString()
        val totalOut = "$minute:$second"
        val totalNew = "$minute:0$second"
        return if (second.length == 1) totalNew else totalOut
    }

    private fun playClickListener() {
        play.setOnClickListener {
            playPauseBtnClicked()
        }
    }

    private fun nextClickListener() {
        next.setOnClickListener {
            nextBtnClicked()
        }
    }

    private fun prevClickListener() {
        previous.setOnClickListener {
            prevBtnClicked()
        }
    }

    private fun stopClickListener() {
        stop.setOnClickListener {
            stopBtnClicked()
        }
    }

    private fun prevNextBtnClicked(song: Song?){
        songService?.playMedia(song)
        initViews(song)
        PlayerNotification.createNotification(applicationContext, song, R.drawable.pause)
        play.setImageResource(R.drawable.pause)
        initializeSeekBar()
    }

    private fun playPauseBtnClicked() {

        if(songService?.isPlaying() == true){
            play.setImageResource(R.drawable.play)
            songService?.pause()
            PlayerNotification.createNotification(
                applicationContext,
                songService?.getCurrentSong(),
                R.drawable.play
            )


        }
        else{
            play.setImageResource(R.drawable.pause)
            songService?.start()
            PlayerNotification.createNotification(
                applicationContext,
                songService?.getCurrentSong(),
                R.drawable.pause
            )

        }
    }

    private fun nextBtnClicked() {
        prevNextBtnClicked(songs[if (songs.indexOf(songService?.getCurrentSong()) == songs.size - 1) 0 else songs.indexOf(songService?.getCurrentSong()) + 1])
    }

    private fun prevBtnClicked() {
        prevNextBtnClicked(songs[if (songs.indexOf(songService?.getCurrentSong()) == 0) songs.size - 1 else songs.indexOf(songService?.getCurrentSong()) - 1])
    }

    private fun stopBtnClicked() {
        songService?.stop()
        songService?.release()
        songService?.create(songService?.getCurrentSong())
        initializeSeekBar()
        play.setImageResource(R.drawable.play)
        PlayerNotification.createNotification(
            applicationContext,
            songService?.getCurrentSong(),
            R.drawable.play
        )

    }

    override fun onStart() {
        super.onStart()
        Intent(this, SongService::class.java).also { intent ->
            bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }
        playClickListener()
        prevClickListener()
        stopClickListener()
        nextClickListener()

        registerReceiver(broadcastReceiver, IntentFilter().also {
            it.addAction(Actions.NEXT.name)
            it.addAction(Actions.PREVIOUS.name)
            it.addAction(Actions.PLAY.name)
            it.addAction(Actions.STOP.name)
        })
    }

    override fun onStop() {
        super.onStop()
        unbindService(myConnection)
        unregisterReceiver(broadcastReceiver)
    }

}


