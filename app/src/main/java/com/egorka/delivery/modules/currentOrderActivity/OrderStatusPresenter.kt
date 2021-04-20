package com.egorka.delivery.modules.currentOrderActivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.egorka.delivery.services.BasePresenter
import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect
import com.google.android.gms.maps.GoogleMap

class OrderStatusPresenter(val view: OrderStatusActivity): BasePresenter, BroadcastReceiver() {

    override var mainService: MainService? = null
    private var serviceConnection = ServiceConnect(this)

    override fun onStart() {
//        view.mMap = GoogleMapDelegate(view)

        var map: GoogleMap?
        view.mapView.getMapAsync { mMap ->
            map = mMap
        }

        val filterMap = IntentFilter("isMapReady")
        view.registerReceiver(this, filterMap)
    }

    fun onCreate() {
        view.bindService(Intent(view.applicationContext, MainService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun drawRoute() {
//        val URL = DrawPolyline(view.mMap.googleMap, view).getDirectionURL(
//            LatLng(55.720268, 37.563071), LatLng(55.622881, 37.666616)
//        )
//        DrawPolyline(view.mMap.googleMap, view).GetDirection(URL).execute()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "isMapReady") {
//            view.mMap.googleMap.setPadding(20, 40, 20, 20)
            drawRoute()
        }

    }


}