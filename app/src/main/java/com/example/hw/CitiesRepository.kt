package com.example.hw

import com.google.gson.annotations.SerializedName

data class CitiesRepository(
    @SerializedName("list")
    val list : List<WeatherResponse>
)


