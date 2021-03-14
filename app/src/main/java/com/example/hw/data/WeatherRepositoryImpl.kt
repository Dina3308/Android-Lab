package com.example.hw.data

import com.example.hw.WeatherResponse
import com.example.hw.data.api.WeatherApi
import com.example.hw.data.api.response.ListWeatherResponse
import com.example.hw.data.db.AppDatabase
import com.example.hw.data.db.dao.ListWeatherDao
import com.example.hw.data.db.dao.WeatherDao
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather
import com.example.hw.domain.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    db: AppDatabase
) : WeatherRepository {

    private val listWeatherDao = db.ListWeatherDao()
    private val weatherDao = db.WeatherDao()

    override suspend fun getWeatherById(id: Int): ListWeather? =
        listWeatherDao.getWeatherById(id)

    override suspend fun getWeatherByName(city: String): WeatherResponse =
        weatherApi.getWeatherByName(city)

    override suspend fun getWeathersByCoord(lat: Double, long: Double): ListWeatherResponse =
        weatherApi.getWeatherCities(lat, long)

    override suspend fun updateWeather(weather: Weather): Weather {
        with(weatherDao){
            deleteWeather()
            insert(weather)
            return weather
        }
    }

    override suspend fun updateListWeather(weathers: List<ListWeather>): List<ListWeather> {
        with(listWeatherDao){
            deleteAllWeathers()
            insertListWeather(weathers)
            return weathers
        }
    }

    override suspend fun getAllWeathersFromDb(): List<ListWeather>? =
        listWeatherDao.getWeathers()

    override suspend fun getWeatherFromDb(): Weather? =
        weatherDao.getWeather()
}