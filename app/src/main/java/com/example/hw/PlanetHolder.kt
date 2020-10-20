package com.example.hw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_planet.*

class PlanetHolder (
    override val containerView: View,
    private val itemClick: (Planet) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var planet: Planet? = null

        init {
            itemView.setOnClickListener {
                planet?.also(itemClick)
            }
        }

        fun bind(planet: Planet) {
            this.planet = planet
            with(planet) {
                namePlanet.text = name
                photoPlanet.setImageResource(photo)
            }
        }

        companion object {

            fun create(parent: ViewGroup, itemClick: (Planet) -> Unit): PlanetHolder =
                PlanetHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_planet, parent, false),
                    itemClick
                )

        }
}