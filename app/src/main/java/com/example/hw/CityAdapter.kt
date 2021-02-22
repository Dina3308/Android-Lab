package com.example.hw

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CityAdapter  (
    private var list: List<WeatherResponse>,
    private val itemClick: (WeatherResponse) -> Unit
) : RecyclerView.Adapter<CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder =
        CityHolder.create(parent, itemClick)

    override fun onBindViewHolder(holder: CityHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}