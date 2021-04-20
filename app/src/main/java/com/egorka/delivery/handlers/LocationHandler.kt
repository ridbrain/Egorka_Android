package com.egorka.delivery.handlers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationHandler(private val context: Activity, val callBack: (Location) -> Unit) {

    var code = 8899
    var location: Location? = null

    private var providerClient: FusedLocationProviderClient? = null

    init {
        providerClient = LocationServices.getFusedLocationProviderClient(context)
        tryLocation()
    }

    fun tryLocation() {

        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), code
            )

        } else {

            providerClient?.lastLocation?.addOnCompleteListener { location ->
                this.location = location.result
                this.location?.let(callBack)
            }

        }

    }

}