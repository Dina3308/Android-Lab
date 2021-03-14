package com.example.hw.presentation.description_weather

import com.example.hw.domain.GetWeatherUseCase
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope

class DescriptionWeatherPresenter(
    private val getWeatherUseCase: GetWeatherUseCase
): MvpPresenter<DescriptionWeatherView>() {

    fun showWeatherById(id: Int){
        presenterScope.launch {
            viewState.showLoading()
            getWeatherUseCase.getWeatherById(id)?.run {
                showWeather(cityName,
                    feelsLike.toInt().toString(),
                    humidity.toString(),
                    pressure.toString(),
                    temp.toInt().toString(),
                    tempMax.toInt().toString(),
                    tempMin.toInt().toString(),
                    cloudiness.toString(),
                    description,
                    icon,
                    speed.toString(),
                    windDirection(deg)
                )
            }
            viewState.hideLoading()
        }
    }

    fun showWeatherFromFromBd(){
        presenterScope.launch {
            viewState.showLoading()
            getWeatherUseCase.getWeatherFromBd()?.run{
                showWeather(cityName,
                    feelsLike.toInt().toString(),
                    humidity.toString(),
                    pressure.toString(),
                    temp.toInt().toString(),
                    tempMax.toInt().toString(),
                    tempMin.toInt().toString(),
                    cloudiness.toString(),
                    description,
                    icon,
                    speed.toString(),
                    windDirection(deg)
                )
            }
            viewState.hideLoading()
        }
    }

    private fun showWeather(city: String,
                            feelsLike: String,
                            humidity: String,
                            pressure: String,
                            temp: String,
                            tempMax: String,
                            tempMin: String,
                            cloudiness: String,
                            description: String,
                            icon: String,
                            speed: String,
                            direction: String
    ){
        with(viewState){
            setCity(city)
            setCloudiness(cloudiness)
            setClouds(description)
            setFeelsLikeTemp(feelsLike)
            setHumidity(humidity)
            setImage(icon)
            setMinMaxTemp(tempMin, tempMax)
            setWind(speed, direction)
            setTemp(temp)
            setPressure(pressure)
        }
    }

    private fun windDirection(deg: Int):String{
        if (22.5 < deg && deg < 337.5){
            return "N"
        }
        else if(22.5 < deg && deg < 67.5){
            return "NE"
        }
        else if(67.5 < deg && deg < 112.5){
            return "E"
        }
        else if(112.5 < deg && deg < 157.5){
            return "SE"
        }
        else if(157.5 < deg && deg < 202.5){
            return "S"
        }
        else if(202.5 < deg && deg < 247.5){
            return "SW"
        }
        else if(247.5 < deg && deg < 292.5){
            return "W"
        }
        else{
            return "NW"
        }
    }
}