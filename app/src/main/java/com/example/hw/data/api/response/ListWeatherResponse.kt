package com.example.hw.data.api.response

import com.example.hw.WeatherResponse
import com.google.gson.annotations.SerializedName

data class ListWeatherResponse(
    @SerializedName("list")
    val list : List<WeatherResponse>
)


