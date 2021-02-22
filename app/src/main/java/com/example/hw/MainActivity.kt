package com.example.hw

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private val api = ApiFactory.weatherApi
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var wayLatitude: Double = 55.7887
    private var wayLongitude: Double = 49.1221
    private var adapter: CityAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
        lifecycleScope.launch {
            if (newText != null) {
                try {
                    api.getWeatherByName(newText).run {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                DescriptionWeatherActivity::class.java
                            ).apply {
                                putExtra("id", id)
                            }
                        )
                    }
                }catch (e: HttpException){
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "город не найден",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            searchView.clearFocus()
            searchView.setQuery("", false);
        }
        return true
    }

    private fun initAdapter(){
        lifecycleScope.launch {
            api.getWeatherCities(wayLatitude, wayLongitude).run {
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
        }
    }
    
    private fun checkForPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                    == PackageManager.PERMISSION_GRANTED){
                mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                    if (location != null) {
                        wayLatitude = location.latitude
                        wayLongitude = location.longitude
                    }
                    initAdapter()
                }
            }
            else {
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
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED){
                        mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                            if (location != null) {
                                wayLatitude = location.latitude
                                wayLongitude = location.longitude
                            }
                            initAdapter()
                        }
                    }
                }
            }
        }
    }
}