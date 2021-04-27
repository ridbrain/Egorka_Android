package com.egorka.delivery.modules.currentOrderActivity

import android.app.Activity
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.entities.OrderLocation
import com.google.android.gms.maps.model.PolylineOptions

interface CurrentOrderActivityInterface {

    fun getContext(): Activity
    fun setPolyline(polylineOptions: PolylineOptions)
    fun updateAdapter(locations: MutableList<OrderLocation>, numState: NumState)

}

interface CurrentOrderPresenterInterface {

    var view: CurrentOrderActivityInterface

    fun onStart()
    fun onResume()
    fun onPause()
    fun onBackPressed()

}