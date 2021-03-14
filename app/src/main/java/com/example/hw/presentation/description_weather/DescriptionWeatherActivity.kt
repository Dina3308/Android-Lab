package com.example.hw.presentation.description_weather

import android.os.Bundle
import android.view.View
import com.example.hw.data.db.AppDatabase
import com.example.hw.R
import com.example.hw.data.WeatherRepositoryImpl
import com.example.hw.data.api.ApiFactory
import com.example.hw.domain.GetWeatherUseCase
import com.example.hw.utils.getErrorMessage
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_description_weather.*
import kotlinx.android.synthetic.main.activity_description_weather.progress_bar
import kotlinx.coroutines.Dispatchers
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class DescriptionWeatherActivity : MvpAppCompatActivity(), DescriptionWeatherView {

    @InjectPresenter
    lateinit var presenter: DescriptionWeatherPresenter

    @ProvidePresenter
    fun providePresenter(): DescriptionWeatherPresenter = initPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description_weather)
        initViews(intent.getIntExtra("id", -1))
    }

    override fun setTemp(temp: String) {
        temp_tv.text = getString(R.string.temp, temp)
    }

    override fun setMinMaxTemp(tempMin: String, tempMax: String) {
        minMaxTemp_tv.text = getString(R.string.min_max_temp, tempMin, tempMax)
    }

    override fun setCity(city: String) {
        nameCity_tv.text = city
    }

    override fun setFeelsLikeTemp(temp: String) {
        feels_like_tv.text = getString(R.string.feels_like, temp)
    }

    override fun setClouds(description: String) {
        clouds_tv.text = description
    }

    override fun setHumidity(humidity: String) {
        humidity_tv.text = getString(R.string.humidity, humidity)
    }

    override fun setCloudiness(cloudiness: String) {
        cloudiness_tv.text = getString(R.string.cloudiness, cloudiness)
    }

    override fun setPressure(pressure: String) {
        pressure_tv.text = getString(R.string.pressure, pressure)
    }

    override fun setWind(speed: String, direction: String) {
        wind_tv.text = getString(R.string.wind, speed, direction )
    }

    override fun setImage(icon: String) {
        weather_iv.setImageResource(resources.getIdentifier(icon, "drawable",
            packageName
        ))
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun consumerError(throwable: Throwable) {
        throwable.getErrorMessage(resources)?.let {
            Snackbar.make(
                findViewById(android.R.id.content),
                it,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun initPresenter() = DescriptionWeatherPresenter(
        getWeatherUseCase = GetWeatherUseCase(
            WeatherRepositoryImpl(ApiFactory.weatherApi, AppDatabase(applicationContext)),
            Dispatchers.IO)
    )

    private fun initViews(id: Int){
        if (id != -1){
            presenter.showWeatherById(id)
        }
        else{
            presenter.showWeatherFromFromBd()
        }
    }
}



