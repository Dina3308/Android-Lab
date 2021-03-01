package com.example.hw.data

import com.example.hw.WeatherResponse
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather

object WeatherConverter {
    fun createWeather(weatherResponse: WeatherResponse): Weather = with(weatherResponse){
        Weather(
            id,
            name,
            main.feelsLike,
            main.humidity,
            main.pressure,
            main.temp,
            main.tempMax,
            main.tempMin,
            clouds.all,
            weather[0].description,
            "w" + weather[0].icon,
            wind.speed,
            wind.deg
        )
    }

    fun createListWeather(list: List<WeatherResponse>): ArrayList<ListWeather> {
        val weatherList = ArrayList<ListWeather>(10)
        for (element in list){
            with(element){
                weatherList.add(
                    ListWeather(
                        id,
                        name,
                        main.feelsLike,
                        main.humidity,
                        main.pressure,
                        main.temp,
                        main.tempMax,
                        main.tempMin,
                        clouds.all,
                        weather[0].description,
                        "w" + weather[0].icon,
                        wind.speed,
                        wind.deg
                    )
                )
            }
        }
        return weatherList
    }
}