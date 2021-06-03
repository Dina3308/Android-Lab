package com.example.hw.presentation.main

import com.example.hw.data.LocationRepositoryImpl
import com.example.hw.domain.GetWeatherUseCase
import kotlinx.coroutines.*
import moxy.MvpPresenter
import moxy.presenterScope
import retrofit2.HttpException
import java.net.UnknownHostException

class MainPresenter(
    private val locationRepository: LocationRepositoryImpl,
    private val getWeatherUseCase: GetWeatherUseCase
):  MvpPresenter<MainView>(){

    private val WAY_LATITUDE: Double = 55.7887
    private val WAY_LONGITUDE: Double = 49.1221

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.checkLocationPermission()
    }

    fun onCityClick(id: Int){
        viewState.navigateToDescriptionWeather(id)
    }

    fun searchCity(city: String?){
        presenterScope.launch {
            try {
                viewState.showLoading()
                city?.let {
                    getWeatherUseCase.getWeatherByName(it)
                    viewState.navigateToDescriptionWeather(-1)
                }
            } catch (ex: HttpException){
                viewState.consumerError(ex)
            } catch (ex: UnknownHostException){
                viewState.consumerError(ex)
                viewState.navigateToDescriptionWeather(-1)
            } finally {
                viewState.hideLoading()
            }
        }
    }

    fun onLocationAccess(){
        presenterScope.launch {
            try {
                viewState.showLoading()
                val location = locationRepository.getUserLocation()
                getWeatherUseCase.getWeathersByCoord(
                    location.latitude,
                    location.longitude
                ).also {
                    viewState.showCities(it)
                }
            } catch (ex: UnknownHostException){
                getWeatherUseCase.getAllWeathersFromDb()?.let {
                    viewState.showCities(it)
                }
                viewState.consumerError(ex)
            } finally {
                viewState.hideLoading()
            }
        }
    }

    fun onLocationDenied(){
        presenterScope.launch {
            try {
                viewState.showLoading()
                getWeatherUseCase.getWeathersByCoord(
                    WAY_LATITUDE,
                    WAY_LONGITUDE
                ).also {
                    viewState.showCities(it)
                }
            } catch (ex: UnknownHostException){
                getWeatherUseCase.getAllWeathersFromDb()?.let {
                    viewState.showCities(it)
                }
                viewState.consumerError(ex)
            } finally {
                viewState.hideLoading()
            }
        }
    }

}