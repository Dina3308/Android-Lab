package com.example.hw.data.db.dao

import androidx.room.*
import com.example.hw.data.db.entity.Weather

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: Weather)

    @Query("DELETE FROM weather")
    suspend fun deleteWeather()

    @Query("SELECT * FROM weather")
    suspend fun getWeather(): Weather?
}