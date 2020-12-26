package com.example.hw.ui.listAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw.R
import com.example.hw.model.entity.Task
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.item_task.*

class TaskHolder (
    override val containerView: View,
    private val deleteClick: (Task) -> Unit,
    private val itemClick: (Task) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var task: Task? = null

        init {
            itemView.setOnClickListener {
                task?.also(itemClick)
            }
            delete.setOnClickListener {
                task?.let { it1 -> deleteClick(it1) }
            }
        }


    fun bind(task: Task) {
        this.task = task
        titleTextView.text = task.title
    }

    companion object {
        fun create(parent: ViewGroup, itemClick: (Task) -> Unit, deleteClick: (Task) -> Unit): TaskHolder =
            TaskHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false),
                itemClick,
                deleteClick
            )
    }
}