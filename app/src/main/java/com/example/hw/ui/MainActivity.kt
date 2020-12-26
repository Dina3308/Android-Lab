package com.example.hw.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hw.R
import com.example.hw.model.AppDatabase
import com.example.hw.model.entity.Task
import com.example.hw.ui.listAdapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var adapter: TaskAdapter? = null
    private lateinit var list: List<Task>
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = AppDatabase(this)
        launch {
            list = ArrayList(db.taskDao().getTasks())
            initAdapter()
        }

        fab.setOnClickListener {
            startActivity(Intent(this, TaskDetail::class.java))
        }

        deleteAll.setOnClickListener {
           deleteAll()
        }
    }

    private fun initAdapter(){
        adapter = TaskAdapter(
            list,
            {task -> delete(task) }
        ) {
            val intent = Intent(this@MainActivity, TaskDetail::class.java).apply {
                putExtra("id", it.id)
            }
            startActivity(intent)
        }

        adapter?.submitList(list)
        rv_task.adapter = adapter
    }

    private fun deleteAll(){
        launch {
            db.taskDao().deleteAllTasks()
            list = db.taskDao().getTasks()
            adapter?.submitList(list)
        }
    }

    private fun delete(task: Task){
        launch {
            db.taskDao().deleteTask(task)
            list = db.taskDao().getTasks()
            adapter?.submitList(list)
        }
    }

    override fun onDestroy() {
        coroutineContext.cancelChildren()
        coroutineContext.cancel()
        super.onDestroy()
    }

}