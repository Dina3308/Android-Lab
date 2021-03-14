package com.example.hw.presentation.main

import com.example.hw.data.db.entity.ListWeather
import com.example.hw.presentation.BaseView
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface MainView : BaseView, MvpView {

    fun checkLocationPermission()

    @OneExecution
    fun navigateToDescriptionWeather(id: Int)

    fun showCities(cities: List<ListWeather>)
}