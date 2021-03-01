package com.example.hw.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.hw.data.db.AppDatabase
import com.example.hw.R
import com.example.hw.data.WeatherRepositoryImpl
import com.example.hw.data.api.ApiFactory
import com.example.hw.domain.GetWeatherUseCase
import kotlinx.android.synthetic.main.activity_description_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DescriptionWeatherActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var getWeatherWeatherUseCase: GetWeatherUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description_weather)

        db = AppDatabase(applicationContext)
        getWeatherWeatherUseCase = GetWeatherUseCase(
            WeatherRepositoryImpl(ApiFactory.weatherApi, db.ListWeatherDao(), db.WeatherDao()),
            Dispatchers.IO)

        initViews(intent.getIntExtra("id", -1))
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

    private fun initViews(id: Int){
        lifecycleScope.launch {
            if (id != -1){
                getWeatherWeatherUseCase.getWeatherById(id)?.run {
                    temp_tv.text = addIcon(temp.toInt(), "°")
                    minMaxTemp_tv.text = addIcon(tempMin.toInt(), "°").plus("/" + addIcon(tempMin.toInt(), "°"))
                    nameCity_tv.text = cityName
                    feels_like_tv.append(" " + addIcon(feelsLike.toInt(), "°"))
                    clouds_tv.text = description
                    humidity_tv.append(" " + addIcon(humidity, " %"))
                    cloudiness_tv.append(" " + addIcon(cloudiness, " %"))
                    pressure_tv.append(" " + addIcon(pressure, " hPa"))
                    wind_tv.append(" " + addIcon(speed, " m/s").plus(" " + windDirection(deg)))
                    weather_iv.setImageResource(resources.getIdentifier(icon, "drawable",
                        packageName
                    ))
                }
            }
            else{
                getWeatherWeatherUseCase.getWeatherFromBd()?.run {
                    temp_tv.text = addIcon(temp.toInt(), "°")
                    minMaxTemp_tv.text = addIcon(tempMin.toInt(), "°").plus("/" + addIcon(tempMin.toInt(), "°"))
                    nameCity_tv.text = cityName
                    feels_like_tv.append(" " + addIcon(feelsLike.toInt(), "°"))
                    clouds_tv.text = description
                    humidity_tv.append(" " + addIcon(humidity, " %"))
                    cloudiness_tv.append(" " + addIcon(cloudiness, " %"))
                    pressure_tv.append(" " + addIcon(pressure, " hPa"))
                    wind_tv.append(" " + addIcon(speed, " m/s").plus(" " + windDirection(deg)))
                    weather_iv.setImageResource(resources.getIdentifier(icon, "drawable",
                        packageName
                    ))
                }
            }
        }
    }
}



