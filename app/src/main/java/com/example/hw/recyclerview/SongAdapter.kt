package com.example.hw.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw.Song

class SongAdapter (
    private var list: List<Song>,
    private val itemClick: (Song) -> Unit
    ) : RecyclerView.Adapter<SongHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder =
            SongHolder.create(parent, itemClick)

        override fun onBindViewHolder(holder: SongHolder, position: Int) =
            holder.bind(list[position])

        override fun getItemCount(): Int = list.size
}
