package com.example.hw.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.hw.R
import com.example.hw.SongRepository
import com.example.hw.recyclerview.SongAdapter
import com.example.hw.service.SongService
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

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, SongService::class.java))
    }
}