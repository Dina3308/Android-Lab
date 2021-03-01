package com.example.hw.domain

import com.example.hw.WeatherResponse
import com.example.hw.data.api.response.ListWeatherResponse
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather

interface WeatherRepository {

    suspend fun getWeatherById(id: Int): ListWeather?

    suspend fun getWeatherByName(city: String): WeatherResponse

    suspend fun getWeathersByCoord(lat: Double, long: Double): ListWeatherResponse

    suspend fun updateWeather(weather: Weather): Weather

    suspend fun updateListWeather(weathers: List<ListWeather>): List<ListWeather>

    suspend fun getAllWeathersFromDb(): List<ListWeather>?

    suspend fun getWeatherFromDb(): Weather?

}