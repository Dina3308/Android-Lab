package com.example.hw.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hw.data.db.dao.ListWeatherDao
import com.example.hw.data.db.dao.WeatherDao
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.data.db.entity.Weather

@Database(entities = [Weather::class, ListWeather::class], version = 2)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun WeatherDao(): WeatherDao
    abstract fun ListWeatherDao(): ListWeatherDao

    companion object {

        private const val DATABASE_NAME = "weather.db"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}