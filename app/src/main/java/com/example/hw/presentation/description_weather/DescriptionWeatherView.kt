package com.example.hw.presentation.description_weather

import com.example.hw.presentation.BaseView
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface DescriptionWeatherView : BaseView, MvpView {

     fun setTemp(temp: String)

     fun setMinMaxTemp(tempMin: String, tempMax: String)

     fun setCity(city: String)

     fun setFeelsLikeTemp(temp: String)

     fun setClouds(description: String)

     fun setHumidity(humidity: String)

     fun setCloudiness(cloudiness: String)

     fun setPressure(pressure: String)

     fun setWind(speed:String, direction: String)

     fun setImage(icon: String)
}