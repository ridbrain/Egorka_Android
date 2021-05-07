package com.egorka.delivery.modules.marketplaceActivity

import android.app.Activity
import com.egorka.delivery.adapters.TypeData
import com.egorka.delivery.entities.Delivery
import com.egorka.delivery.entities.Dictionary
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.services.BottomState
import java.util.*

interface MarketplaceActivityInterface {

    fun getContext(): Activity
    fun setBottomSheetState(state: BottomState)
    fun pressPlaces(locations: List<OrderLocation>)
    fun setPickupLabel(text: String)
    fun setPlaceLabel(text: String)
    fun setSuggestions(suggestion: List<Dictionary.Suggestion>)
    fun setFocus(size: Int)
    fun deleteFocus()
    fun showSuggestions(show: Boolean)
    fun setInfoFields(type: TypeData, price: Delivery.TotalPrice)
    fun hideBottomSheet()
    fun showDatePicker(calendar: Calendar)
    fun setDate(text: String)
    fun setBoxLabel(text: String)
    fun setPalletLabel(text: String)
    fun changeIconFirstField(edit: Boolean)
    fun addressFieldFocused(): Boolean
    fun clearAddressField()

}

interface MarketplacePresenterInterface {

    fun onResume()
    fun bottomStateChanged(state: Int)
    fun hideKeyboard()
    fun pressAddressButton()
    fun pressPlaces()
    fun selectPlace(location: OrderLocation)
    fun selectTextField()
    fun textDidChange(text: String)
    fun selectAddress(address: Dictionary.Suggestion)
    fun pressDate()
    fun setDate(year: Int, month: Int, day: Int)
    fun openMarketMap()
    fun boxCountChange(count: Int)
    fun palletCountChange(count: Int)

}