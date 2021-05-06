package com.egorka.delivery.modules.marketMap

import android.app.Activity
import com.egorka.delivery.entities.OrderLocation

interface MarketMapActivityInterface {

    fun getContext(): Activity
    fun setPoints(points: List<OrderLocation>)

}

interface MarketMapPresenterInterface {

    var view: MarketMapActivityInterface

    fun onResume()
    fun selectLocation(location: OrderLocation)

}