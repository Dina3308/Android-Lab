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
        val id = intent.getStringExtra("id")
        planetes.find { it.id = id}?.also {
            setContent(it)
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
