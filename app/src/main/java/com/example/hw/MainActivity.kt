package com.example.hw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var adapter: PlanetAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PlanetAdapter(
            PlanetRepository.getPlanets()
        ) {
           val intent = Intent(this, ContentActivity::class.java)
           intent.putExtra("id", it.id)
           startActivity(intent)
        }
        rv_planet.adapter = adapter
        rv_planet.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}