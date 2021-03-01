package com.example.hw.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ListWeather")
data class ListWeather(
    @PrimaryKey
    var id: Int,
    @ColumnInfo(name = "city_name")
    var cityName: String,
    @ColumnInfo(name = "feelsLike")
    var feelsLike: Double,
    @ColumnInfo(name = "humidity")
    var humidity: Int,
    @ColumnInfo(name = "pressure")
    var pressure: Int,
    @ColumnInfo(name = "temp")
    var temp: Double,
    @ColumnInfo(name = "tempMax")
    var tempMax: Double,
    @ColumnInfo(name = "tempMin")
    var tempMin: Double,
    @ColumnInfo(name = "cloudiness")
    var cloudiness: Int,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "icon")
    var icon: String,
    @ColumnInfo(name = "speed")
    var speed: Double,
    @ColumnInfo(name = "deg")
    var deg: Int
)

