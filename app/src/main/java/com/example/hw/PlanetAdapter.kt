package com.example.hw

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlanetAdapter (
    private var list: List<Planet>,
    private val itemClick: (Planet) -> Unit
    ) : RecyclerView.Adapter<PlanetHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetHolder =
            PlanetHolder.create(parent, itemClick)

        override fun onBindViewHolder(holder: PlanetHolder, position: Int) =
            holder.bind(list[position])

        override fun getItemCount(): Int = list.size
}
