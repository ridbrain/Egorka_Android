package com.egorka.delivery.modules.mainActivity

import android.app.Activity
import com.egorka.delivery.adapters.TypeDelivery
import com.egorka.delivery.entities.Suggestion
import com.egorka.delivery.services.BottomState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

interface MainActivityInterface {

    fun getContext(): Activity
    fun getMapLocation(): CameraPosition?
    fun setAddress(field: FocusState, address: String)
    fun setMapRegion(latLng: LatLng)
    fun setSuggestions(suggestion: List<Suggestion>)
    fun setBottomSheetState(state: BottomState)
    fun getFocusField(): FocusState
    fun showDropEditText(show: Boolean)
    fun setPolyline(polylineOptions: PolylineOptions)
    fun deletePolyline()
    fun showSuggestionsRecyclerView(show: Boolean)
    fun getDropText(): String
    fun getPickupText(): String
    fun showButtons(show: Boolean)
    fun showChangeAlert(callback: (Boolean) -> Unit)
    fun showPin(show: Boolean)
    fun setFocus(field: FocusState, size: Int)
    fun deleteFocus(focusState: FocusState)
    fun defaultAnimateCamera()
    fun changeIconFirstField(edit: Boolean)
    fun showIconSecondField(show: Boolean)
    fun showWarning()
    fun updateTypeAdapter(types: List<TypeDelivery>)
    fun clearFocus()

}

interface MainPresenterInterface {

    var view: MainActivityInterface

    fun onStart()
    fun onResume()
    fun onPause()
    fun onBackPressed()
    fun selectTextField()
    fun textDidChange(text: String)
    fun changeCameraPosition()
    fun hideKeyboard()
    fun requestPermissions(code: Int)
    fun selectAddress(address: Suggestion)
    fun bottomStateChanged(state: Int)
    fun didRouteLaid()
    fun pressPickupFieldButton()
    fun pressDropFieldButton()
    fun selectTypeDelivery(type: Int)

}

enum class FocusState {
    First,
    Second
}

enum class Causes {
    ChangeRegion,
    SelectField
}