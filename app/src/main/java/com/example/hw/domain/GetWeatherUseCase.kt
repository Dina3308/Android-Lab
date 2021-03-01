package com.example.hw.domain

import com.example.hw.WeatherResponse
import com.example.hw.data.WeatherConverter
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val context: CoroutineContext
) {

    suspend fun getWeatherByName(city: String): Weather =
        withContext(context) {
            weatherRepository.updateWeather(WeatherConverter.createWeather(weatherRepository.getWeatherByName(city)))
        }

    suspend fun getWeathersByCoord(lat: Double, long: Double): List<ListWeather> =
        withContext(context) {
            weatherRepository.updateListWeather(WeatherConverter.createListWeather(weatherRepository.getWeathersByCoord(lat, long).list))
        }

    suspend fun getAllWeathersFromDb(): List<ListWeather>? =
        withContext(context){
            weatherRepository.getAllWeathersFromDb()
        }

    suspend fun getWeatherById(id: Int): ListWeather? =
        withContext(context){
            weatherRepository.getWeatherById(id)
        }

    suspend fun getWeatherFromBd(): Weather? =
        withContext(context){
            weatherRepository.getWeatherFromDb()
        }

}