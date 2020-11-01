package com.example.hw

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(
    private var list: ArrayList<Note>
) : RecyclerView.Adapter<NoteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder =
        NoteHolder.create(parent)

    override fun onBindViewHolder(holder: NoteHolder, position: Int){
        holder.bind(list[position])
        holder.itemView.delete.setOnClickListener {
            val newList = ArrayList<Note>(list).also {
                it.removeAt(position)
            }
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount - position);
            update(newList)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun update(newList: ArrayList<Note>) {
        val callback = NoteDiffCallback(list, newList)
        val result = DiffUtil.calculateDiff(callback, true)
        result.dispatchUpdatesTo(this)
        list = newList
    }

    fun addElement(position : String, title: String, description: String){
        var id = 5
        if (title.isNotEmpty() && description.isNotEmpty()){
            val newList = ArrayList<Note>(list).also {
                val element = Note(title, description, id++)
                if(position.isEmpty() ||  position.toInt() > list.size){
                    it.add(element)
                }
                else{
                    it.add(position.toInt() - 1, element)
                }
            }
            update(newList)
        }
    }
}