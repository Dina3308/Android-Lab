package com.example.hw

import android.graphics.Color.parseColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_city.*

class CityHolder (
    override val containerView: View,
    private val itemClick: (WeatherResponse) -> Unit
    ): RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var city: WeatherResponse? = null

        init {
            itemView.setOnClickListener {
                city?.also(itemClick)
            }
        }

        fun bind(city: WeatherResponse) {
            this.city = city
            with(city) {
                name_tv.text = name
                temp_tv.setTextColor(parseColor(getColor(main.temp.toInt())))
                temp_tv.text = main.temp.toInt().toString().plus("°")
            }
        }

        companion object {
            fun create(parent: ViewGroup, itemClick: (WeatherResponse) -> Unit): CityHolder =
                CityHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false),
                    itemClick
                )
        }


        private fun getColor(temp: Int): String{
            return if (temp <= -20){
                Color.BLUE.rgb
            } else if(temp <= 0 && temp > -20){
                Color.GREEN.rgb
            } else if(temp in 1..15){
                Color.YELLOW.rgb
            } else if(temp in 16..25){
                Color.ORANGE.rgb
            } else{
                Color.RED.rgb
            }
        }
}