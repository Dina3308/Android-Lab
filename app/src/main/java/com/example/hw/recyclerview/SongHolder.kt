package com.example.hw.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw.R
import com.example.hw.Song
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_song.view.*


class SongHolder (
    override val containerView: View,
    private val itemClick: (Song) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var song: Song? = null

        init {
            itemView.setOnClickListener {
                song?.also(itemClick)
            }
        }

        fun bind(song: Song) {
            this.song = song
            with(song) {
                itemView.name.text = name
                itemView.singer.text = singer
                itemView.duration.text = duration
            }
        }

        companion object {

            fun create(parent: ViewGroup, itemClick: (Song) -> Unit): SongHolder =
                SongHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false),
                    itemClick
                )

        }
}