package com.example.hw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.view.*

class NoteHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(note: Note) {
        this.note = note
        with(note) {
            containerView.descr.text = description
            containerView.titleNote.text = title
        }

    }

    companion object {
        fun create(parent: ViewGroup): NoteHolder =
            NoteHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
            )
    }
}
