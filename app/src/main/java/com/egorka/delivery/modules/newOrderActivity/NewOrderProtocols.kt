package com.egorka.delivery.modules.newOrderActivity

import android.app.Activity
import android.view.View
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.adapters.TypeData
import com.egorka.delivery.entities.Delivery
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.services.BottomState

interface NewOrderActivityInterface {

    fun getContext(): Activity
    fun updateAdapters(pickups: MutableList<OrderLocation>, drops: MutableList<OrderLocation>, numState: NumState)
    fun getTransitionName(): String
    fun setInfoFields(type: TypeData, price: Delivery.TotalPrice)
    fun setBottomSheetState(state: BottomState)
    fun hideBottomSheet()

}

interface NewOrderPresenterInterface {

    fun onResume()
    fun openDetails(location: OrderLocation, index: Int, view: View)
    fun bottomStateChanged(state: Int)
    fun newPickup()
    fun newDrop()
    fun deleteLocation(routeOrder: Int)
    fun hideBottomView()
    fun hideKeyboard()

}