package com.example.hw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_description_weather.*
import kotlinx.coroutines.launch

class DescriptionWeatherActivity : AppCompatActivity() {

    private val api = ApiFactory.weatherApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description_weather)
        initViews()
    }

    private fun <T> addIcon(temp: T, icon: String): String = temp.toString().plus(icon)

    private fun windDirection(deg: Int):String{
        if (22.5 < deg && deg < 337.5){
            return "N"
        }
        else if(22.5 < deg && deg < 67.5){
            return "NE"
        }
        else if(67.5 < deg && deg < 112.5){
            return "E"
        }
        else if(112.5 < deg && deg < 157.5){
            return "SE"
        }
        else if(157.5 < deg && deg < 202.5){
            return "S"
        }
        else if(202.5 < deg && deg < 247.5){
            return "SW"
        }
        else if(247.5 < deg && deg < 292.5){
            return "W"
        }
        else{
            return "NW"
        }
    }

    private fun initViews(){
        lifecycleScope.launch {
            api.getWeatherById(intent.getIntExtra("id", 0)).run {
                Picasso.get()
                    .load("https://openweathermap.org/img/wn/${weather[0].icon}@2x.png")
                    .into(weather_iv)
                temp_tv.text = addIcon(main.temp.toInt(), "°")
                minMaxTemp_tv.text = addIcon(main.tempMin.toInt(), "°").plus("/" + addIcon(main.tempMin.toInt(), "°"))
                nameCity_tv.text = name
                feels_like_tv.append(" " + addIcon(main.feelsLike.toInt(), "°"))
                clouds_tv.text = weather[0].description
                humidity_tv.append(" " + addIcon(main.humidity, " %"))
                cloudiness_tv.append(" " + addIcon(clouds.all, " %"))
                pressure_tv.append(" " + addIcon(main.pressure, " hPa"))
                wind_tv.append(" " + addIcon(wind.speed, " m/s").plus(" " + windDirection(wind.deg)))
            }
        }
    }
}



