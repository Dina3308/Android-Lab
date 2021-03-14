package com.example.hw.data.api

import com.example.hw.WeatherResponse
import com.example.hw.data.api.response.ListWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherByName(
        @Query("q") cityName: String
    ) : WeatherResponse

    @GET("weather")
    suspend fun getWeatherById(
        @Query("id") cityId: Int
    ) : WeatherResponse

    @GET("find?cnt=10")
    suspend fun getWeatherCities(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
        ) : ListWeatherResponse
}