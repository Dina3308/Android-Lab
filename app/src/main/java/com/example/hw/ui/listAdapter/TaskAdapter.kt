package com.example.hw.ui.listAdapter


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.hw.model.entity.Task

class TaskAdapter(
    private var list: List<Task>,
    private val deleteClick: (Task) -> Unit,
    private val itemClick: (Task) -> Unit

) : ListAdapter<Task, TaskHolder>(object : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder =
        TaskHolder.create(parent, deleteClick, itemClick)

    override fun onBindViewHolder(holder: TaskHolder, position: Int) =
        holder.bind(list[position])

}