package com.example.hw.presentation.main.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw.data.db.entity.ListWeather

class CityAdapter  (
    private var list: List<ListWeather>,
    private val itemClick: (ListWeather) -> Unit
) : RecyclerView.Adapter<CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder =
        CityHolder.create(parent, itemClick)

    override fun onBindViewHolder(holder: CityHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}