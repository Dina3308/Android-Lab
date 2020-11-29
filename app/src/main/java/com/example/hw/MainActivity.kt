package com.example.hw

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter: SongAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = SongAdapter(
            SongRepository.getSongs()
        ) {
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra("song", it)
            }
            startActivity(intent)
        }
        rv_song.adapter = adapter
    }

}