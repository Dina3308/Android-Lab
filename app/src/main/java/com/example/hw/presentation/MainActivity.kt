package com.example.hw.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hw.*
import com.example.hw.data.LocationUtils
import com.example.hw.data.NetworkConnection
import com.example.hw.data.WeatherRepositoryImpl
import com.example.hw.data.api.ApiFactory
import com.example.hw.data.db.AppDatabase
import com.example.hw.data.db.entity.ListWeather
import com.example.hw.domain.GetWeatherUseCase
import com.example.hw.presentation.recyclerview.CityAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.HttpException

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private lateinit var mFusedLocationClient: LocationUtils
    private val WAY_LATITUDE: Double = 55.7887
    private val WAY_LONGITUDE: Double = 49.1221
    private var adapter: CityAdapter? = null
    private lateinit var db: AppDatabase
    private lateinit var getWeatherWeatherUseCase: GetWeatherUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationUtils(applicationContext)
        db = AppDatabase(applicationContext)
        getWeatherWeatherUseCase = GetWeatherUseCase(
            WeatherRepositoryImpl(ApiFactory.weatherApi, db.ListWeatherDao(), db.WeatherDao()),
            Dispatchers.IO)

        checkForPermissions()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return searchCity(query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun searchCity(newText: String?): Boolean {
        if (newText != null) {
            if (NetworkConnection.isConnected(this@MainActivity)){
                try {
                    lifecycleScope.launch() {
                        getWeatherWeatherUseCase.getWeatherByName(newText).run {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    DescriptionWeatherActivity::class.java
                                ).apply {
                                    putExtra("id", -1)
                                }
                            )
                        }
                    }
                }catch (e: HttpException){
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "город не найден",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else{
                startActivity(
                    Intent(
                        this@MainActivity,
                        DescriptionWeatherActivity::class.java
                    )
                )
            }
        }
        searchView.clearFocus()
        searchView.setQuery("", false)
        return true
    }

    private fun initAdapter(list: List<ListWeather>){
        adapter = CityAdapter(
            list
        ) {
            val intent = Intent(this@MainActivity, DescriptionWeatherActivity::class.java).apply {
                putExtra("id", it.id)
            }
            startActivity(intent)
        }
        rv_city.adapter = adapter
    }

    private fun initViews(wayLatitude: Double, wayLongitude: Double){
        if (NetworkConnection.isConnected(this)){
            lifecycleScope.launch {
                getWeatherWeatherUseCase.getWeathersByCoord(wayLatitude, wayLongitude).run {
                    initAdapter(this)
                }
            }
        } else{
            lifecycleScope.launch {
                getWeatherWeatherUseCase.getAllWeathersFromDb()?.let {
                    initAdapter(it)
                }
            }
        }
    }

    private fun checkForPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                    == PackageManager.PERMISSION_GRANTED){
                val location = mFusedLocationClient.getLocation(this)
                if(location != null){
                    initViews(location.latitude, location.longitude)
                }
                else{
                    initViews(WAY_LATITUDE, WAY_LONGITUDE)
                }
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
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
                    val location = mFusedLocationClient.getLocation(this)
                    if(location != null){
                        initViews(location.latitude, location.longitude)
                    }
                    else{
                        initViews(WAY_LATITUDE, WAY_LONGITUDE)
                    }
                } else{
                    initViews(WAY_LATITUDE, WAY_LONGITUDE)
                }
            }
        }
    }

}