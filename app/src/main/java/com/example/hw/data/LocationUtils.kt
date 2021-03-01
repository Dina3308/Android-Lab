package com.example.hw.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationUtils(context: Context) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private  var loc: Location? = null

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity) : Location? {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(activity) { location ->
            if (location != null) {
                loc = location
            }
        }
        return loc
    }
}



