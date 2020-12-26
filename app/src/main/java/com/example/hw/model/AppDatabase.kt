package com.example.hw.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hw.model.dao.TaskDao
import com.example.hw.model.entity.Task
import com.example.hw.ui.MainActivity

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        private const val DATABASE_NAME = "toDoList.db"

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
