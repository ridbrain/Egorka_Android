package com.egorka.delivery.handlers

import android.app.Activity
import android.location.Geocoder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.egorka.delivery.R
import com.egorka.delivery.entities.Point
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import java.io.IOException

class GoogleMapHandler(val context: Activity) {

    private val geocoder = Geocoder(context)

    fun getAddress(coordinate: LatLng, callBack: (String) -> Unit) {

        try {

            val addressList = geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)

            if (addressList.isNotEmpty()) {

                if (addressList[0].thoroughfare == null) { return }
                if (addressList[0].subThoroughfare == null) { return }

                callBack(addressList[0].thoroughfare.plus(", ").plus(addressList[0].subThoroughfare))

            }

        } catch (e: IOException) {

            callBack("Нет адреса")

        }

    }

    fun getRoute(pickup: Point, drop: Point, callback: (PolylineOptions) -> Unit) {

        val origin = pickup.Latitude.toString() + "," + pickup.Longitude.toString()
        val destination = drop.Latitude.toString() + "," + drop.Longitude.toString()
        val key = context.resources.getString(R.string.map_key)

        NetworkHandler(context).getPolyline(origin, destination, key) { routes ->

            val result =  ArrayList<List<LatLng>>()
            val path =  ArrayList<LatLng>()

            routes.legs?.get(0)?.steps?.forEach { step ->
                path.addAll(decodePolyline(step.polyline!!.points!!))
            }

            result.add(path)

            val lineOption = PolylineOptions()
            for(i in result.indices) {
                lineOption.addAll(result[i])
                lineOption.width(13f)
                lineOption.color(ContextCompat.getColor(context, R.color.colorGreen))
                lineOption.geodesic(true)
            }

            callback(lineOption)

        }

    }

    private fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {

            var b: Int
            var shift = 0
            var result = 0

            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)

            lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            shift = 0
            result = 0

            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)

            lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))

            poly.add(latLng)

        }

        return poly

    }

    fun getCameraPosition(points: List<LatLng>, padding: Int): CameraUpdate {

        val builder = LatLngBounds.Builder()

        points.forEach {
            builder.include(it)
        }

        return CameraUpdateFactory.newLatLngBounds(builder.build(), padding)

    }

    fun getMarker(latLng: LatLng, icon: Int, size: Int): MarkerOptions {

        val marker = MarkerOptions().position(latLng)

        ContextCompat.getDrawable(context, icon)?.let {
            marker.icon(BitmapDescriptorFactory.fromBitmap(it.toBitmap(size, size, null)))
        }

        return marker

    }

}