package com.example.hw.presentation.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hw.*
import com.example.hw.data.LocationRepositoryImpl
import com.example.hw.data.WeatherRepositoryImpl
import com.example.hw.data.api.ApiFactory
import com.example.hw.data.db.AppDatabase
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.domain.GetWeatherUseCase
import com.example.hw.presentation.description_weather.DescriptionWeatherActivity
import com.example.hw.presentation.main.recyclerview.CityAdapter
import com.example.hw.utils.getErrorMessage
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class MainActivity :  MvpAppCompatActivity(), MainView {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = initPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSearchView()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    presenter.onLocationAccess()
                } else{
                    presenter.onLocationDenied()
                }
            }
        }
    }

    override fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED){
                presenter.onLocationAccess()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun navigateToDescriptionWeather(id: Int) {
        startActivity(Intent(this@MainActivity, DescriptionWeatherActivity::class.java).apply {
            putExtra("id", id)
        })
    }

    override fun showCities(cities: List<ListWeather>) {
        val adapter = CityAdapter(
            cities
        ) {
            presenter.onCityClick(it.id)
        }
        rv_city.adapter = adapter
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

    private fun initPresenter() = MainPresenter(
        locationRepository = LocationRepositoryImpl(
            client = LocationServices.getFusedLocationProviderClient(this)
        ),
        getWeatherUseCase = GetWeatherUseCase(
            WeatherRepositoryImpl(ApiFactory.weatherApi, AppDatabase(applicationContext)),
            Dispatchers.IO)
    )

    private fun initSearchView(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.searchCity(query)
                searchView.clearFocus()
                searchView.setQuery("", false)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}