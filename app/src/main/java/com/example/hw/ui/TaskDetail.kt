package com.example.hw.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hw.R
import com.example.hw.model.AppDatabase
import com.example.hw.model.entity.Task
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TaskDetail : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var db: AppDatabase
    private var id: Int? = null
    private  var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        init()

        fabSave.setOnClickListener {
            val titleText = titleEditText.text.toString()
            val descText = descriptionEditText.text.toString()

            launch {
                if(id == -1){
                    db.taskDao().save(Task(if (titleText.isEmpty()) "without the title" else titleText,  descText))
                }
                else{
                    task?.run {
                        title = if(titleText.isEmpty()) "without the title" else titleText
                        description = descText
                        db.taskDao().updateTask(this)
                    }
                }
                startActivity(Intent(this@TaskDetail, MainActivity::class.java))
            }
        }
    }

    private fun init() {
        db = AppDatabase(this)
        id = intent.getIntExtra("id", -1)
        launch {
            if (id != -1){
                id?.let {
                    task = db.taskDao().getTaskById(it)
                    titleEditText.setText(task?.title)
                    descriptionEditText.setText(task?.description)
                }
            }
        }
    }

}

