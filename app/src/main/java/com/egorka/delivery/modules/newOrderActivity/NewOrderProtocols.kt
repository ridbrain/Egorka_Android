package com.egorka.delivery.modules.newOrderActivity

import android.app.Activity
import android.view.View
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.entities.NewOrderLocation
import com.egorka.delivery.services.BottomState

interface NewOrderActivityInterface {

    fun getContext(): Activity
    fun updateAdapters(pickups: MutableList<NewOrderLocation>, drops: MutableList<NewOrderLocation>, numState: NumState)
    fun getTransitionName(): String
    fun setBottomSheetState(state: BottomState)
    fun hideBottomSheet()

}

interface NewOrderPresenterInterface {

    fun onStart()
    fun onResume()
    fun openDetails(location: NewOrderLocation, index: Int, view: View)
    fun bottomStateChanged(state: Int)
    fun newPickup()
    fun newDrop()
    fun removePickup(index: Int)
    fun removeDrop(index: Int)
    fun openKeyboard()
    fun hideKeyboard()

}