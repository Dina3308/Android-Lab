package com.example.hw

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_content.*


class ContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val planets = PlanetRepository.getPlanets()
        when (intent.getStringExtra("id")) {
            planets[0].id -> setContent(planets[0])
            planets[1].id -> setContent(planets[1])
            planets[2].id -> setContent(planets[2])
            planets[3].id -> setContent(planets[3])
            planets[4].id -> setContent(planets[4])
            planets[5].id -> setContent(planets[5])
            planets[6].id -> setContent(planets[6])
            planets[7].id -> setContent(planets[7])
        }
    }

    private fun setContent(planet: Planet){
        textName.text = setBold("Название: ".plus(planet.name), 0, 8)
        planetImage.setImageResource(planet.photo)
        content.text = setBold("Описание: ".plus(planet.description), 0, 8)
    }

    private fun setBold(text: String, start: Int, end: Int): SpannableString {
        val ss = SpannableString(text)
        ss.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ss
    }
}