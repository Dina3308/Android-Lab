package com.example.hw.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hw.data.db.entity.ListWeather

@Dao
interface ListWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListWeather(weathers: List<ListWeather>)

    @Query("DELETE FROM ListWeather")
    suspend fun deleteAllWeathers()

    @Query("SELECT * FROM ListWeather WHERE id = :id")
    suspend fun getWeatherById(id: Int): ListWeather?

    @Query("SELECT * FROM ListWeather")
    suspend fun getWeathers(): List<ListWeather>?
}