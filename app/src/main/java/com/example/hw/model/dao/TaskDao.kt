package com.example.hw.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hw.model.entity.Task


@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id: Int): Task?

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task")
    suspend fun deleteAllTasks()

}