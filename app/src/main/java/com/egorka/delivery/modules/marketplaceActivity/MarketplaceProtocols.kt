package com.egorka.delivery.modules.marketplaceActivity

import android.app.Activity
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.services.BottomState

interface MarketplaceActivityInterface {

    fun getContext(): Activity
    fun setBottomSheetState(state: BottomState)
    fun pressPlaces(locations: List<OrderLocation>)
    fun setPlaceLabel(text: String)

}

interface MarketplacePresenterInterface {

    fun onResume()
    fun bottomStateChanged(state: Int)
    fun hideKeyboard()
    fun pressPlaces()
    fun selectPlace(location: OrderLocation)

}