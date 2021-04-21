package com.egorka.delivery.modules.mainActivity

import android.os.Handler
import android.os.Looper
import com.egorka.delivery.R
import com.egorka.delivery.adapters.TypeDelivery
import com.egorka.delivery.handlers.LocationHandler
import com.egorka.delivery.entities.LocationType
import com.egorka.delivery.entities.NewOrderLocation
import com.egorka.delivery.entities.Suggestion
import com.egorka.delivery.handlers.GoogleMapHandler
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.egorka.delivery.handlers.NetworkHandler
import com.egorka.delivery.services.*
import java.util.ArrayList

class MainPresenter(override var view: MainActivityInterface): MainPresenterInterface {

    private var mainService: MainService? = null
    private var locationHandler: LocationHandler? = null
    private var myLocation = false
    private var routeLaid = false
    private var keyboardHide = true

    init { ServiceConnect(view.getContext()) { mainService = it ; onStart() } }

    override fun onStart() {

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBottomSheetState(BottomState.Small)
        }, 500)

        locationHandler = LocationHandler(view.getContext()) { location ->
            if (!myLocation) {
                view.setMapRegion(LatLng(location.latitude,location.longitude))
                myLocation = true
            }
        }

    }

    override fun onResume() {

        if (routeLaid) {
            deleteRoute(Causes.ChangeRegion)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            moveMyLocation()
        }, 500)

    }

    override fun onPause() {

        Handler(Looper.getMainLooper()).postDelayed({
            view.deletePolyline()
            view.setBottomSheetState(BottomState.Small)
            view.showButtons(false)
        }, 500)

    }

    override fun onBackPressed() {

    }

    override fun changeCameraPosition() {

        if (routeLaid) {

            view.showChangeAlert { answer ->

                if (answer) {
                    deleteRoute(Causes.ChangeRegion)
                } else {
                    view.defaultAnimateCamera()
                }

            }

        } else {

            getAddress()

        }

    }

    private fun getAddress() {
        view.getMapLocation()?.target?.let { coordinate ->
            if (coordinate != LatLng(0.0,0.0)) {
                GoogleMapHandler(view.getContext()).getAddress(coordinate) { address ->
                    NetworkHandler(view.getContext()).searchAddress(address) { suggestions ->
                        mainService?.mainModel?.pickups = mutableListOf(NewOrderLocation(1, LocationType.Pickup, suggestions[0]))
                        view.setAddress(FocusState.First, suggestions[0].Name!!)
                        view.setSuggestions(suggestions)
                    }
                }
            }
        }
    }

    override fun selectTextField() {

        if (keyboardHide) {

            openBottomView()

            if (routeLaid) {

                view.showChangeAlert { answer ->

                    if (answer) {
                        deleteRoute(Causes.SelectField)
                    } else {
                        hideKeyboard()
                    }

                }

            }

        }

        keyboardHide = false

        when (view.getFocusField()) {
            FocusState.First -> {
                view.changeIconFirstField(true)
                view.showIconSecondField(false)
            }
            FocusState.Second -> {
                view.changeIconFirstField(false)
                view.showIconSecondField(true)
            }
        }

    }

    private fun openBottomView() {

        view.showButtons(false)
        view.setBottomSheetState(BottomState.Big)

        if (view.getFocusField() == FocusState.First) {
            view.showDropEditText(false)
        }

    }

    override fun textDidChange(text: String) {
        NetworkHandler(view.getContext()).searchAddress(text) { suggestions ->
            view.setSuggestions(suggestions)
        }
    }

    override fun hideKeyboard() {

        if (!keyboardHide) {

            view.showButtons(routeLaid)
            view.showDropEditText(true)
            view.changeIconFirstField(false)
            view.showIconSecondField(false)
            view.getContext().hideKeyboard()

            keyboardHide = true

            if (routeLaid) {
                view.setBottomSheetState(BottomState.Medium)
            } else {
                view.setBottomSheetState(BottomState.Small)
            }

        }

    }

    override fun requestPermissions(code: Int) {
        if (code == locationHandler?.code) {
            locationHandler?.tryLocation()
        }
    }

    override fun selectAddress(address: Suggestion) {

        address.Name?.let {
            view.getFocusField().let { focus ->
                view.setAddress(focus, it)
                view.setFocus(focus, it.length)
            }
        }

        if (address.ID != null) {

            when(view.getFocusField()) {
                FocusState.First -> {

                    mainService?.mainModel?.pickups = mutableListOf(NewOrderLocation(1, LocationType.Pickup, address))
                    view.showDropEditText(true)

                    address.Point?.let {
                        view.setMapRegion(LatLng(it.Latitude!!, it.Longitude!!))
                    }

                    mainService?.mainModel?.drops?.get(0)?.Point?.let {

                        if (it.Code != address.ID) {

                            view.deleteFocus(FocusState.First)
                            hideKeyboard()
                            setRoute()
                            return

                        }

                        view.showWarning()

                    }

                    view.setFocus(FocusState.Second, view.getDropText().length)

                }
                FocusState.Second -> {

                    mainService?.mainModel?.drops = mutableListOf(NewOrderLocation(2, LocationType.Drop, address))

                    mainService?.mainModel?.pickups?.get(0)?.Point?.let {

                        if (it.Code != address.ID) {

                            view.deleteFocus(FocusState.Second)
                            hideKeyboard()
                            setRoute()
                            return

                        }

                        view.showWarning()

                    }

                }
            }

        }

    }

    private fun setRoute() {
        if (mainService?.mainModel?.drops?.size != null && mainService?.mainModel?.pickups?.size != null) {
            if (view.getPickupText() == mainService?.mainModel?.pickups!![0].Point?.Address
                    && view.getDropText() == mainService?.mainModel?.drops!![0].Point?.Address
            ) {
                GoogleMapHandler(view.getContext()).getRoute(mainService?.mainModel?.pickups!![0].Point!!, mainService?.mainModel?.drops!![0].Point!!) {
                    view.setPolyline(it)
                    view.showPin(false)
                }
            }
        }
    }

    override fun didRouteLaid() {

        routeLaid = true
        setTypesDelivery()

    }

    private fun setTypesDelivery() {

        val types = ArrayList<TypeDelivery>()

        types.add(TypeDelivery(1, "Пеший", 120f, R.drawable.ic_leg))
        types.add(TypeDelivery(2, "Скутер", 130f, R.drawable.ic_bike))
        types.add(TypeDelivery(3, "Легковой", 140f, R.drawable.ic_car))
        types.add(TypeDelivery(4, "Грузовой", 150f, R.drawable.ic_track))

        view.updateTypeAdapter(types)
        view.showButtons(true)
        view.setBottomSheetState(BottomState.Medium)

    }

    override fun pressPickupFieldButton() {

        if (view.getFocusField() == FocusState.First && !keyboardHide) {
            view.setAddress(FocusState.First, "")
            view.setFocus(FocusState.First, 0)
        } else {
            moveMyLocation()
        }

    }

    override fun pressDropFieldButton() {

        if (view.getFocusField() == FocusState.Second && !keyboardHide) {
            view.setAddress(FocusState.Second, "")
            view.setFocus(FocusState.Second, 0)
        }

    }

    override fun selectTypeDelivery(type: Int) {

        GeneralRouter(view.getContext()).openNewOrderActivity()

    }

    private fun deleteRoute(causes: Causes) {

        routeLaid = false

        mainService?.mainModel?.pickups = null
        mainService?.mainModel?.drops = null

        view.deletePolyline()
        view.showPin(true)

        when(causes) {
            Causes.SelectField -> {
                when(view.getFocusField()) {
                    FocusState.First -> {
                        view.setAddress(FocusState.First, "")
                        view.setAddress(FocusState.Second, "")
                        view.setFocus(FocusState.First, 0)
                    }
                    FocusState.Second -> {
                        view.setAddress(FocusState.Second, "")
                        view.setFocus(FocusState.Second, 0)
                    }
                }
            }
            Causes.ChangeRegion -> {
                view.setAddress(FocusState.First, "")
                view.setAddress(FocusState.Second, "")
                view.setBottomSheetState(BottomState.Small)
                view.showButtons(false)
                getAddress()
            }
        }

    }

    private fun moveMyLocation() {
        locationHandler?.location?.let {
            view.setMapRegion(LatLng(it.latitude, it.longitude))
        }
    }

    override fun bottomStateChanged(state: Int) {

        if (state == BottomSheetBehavior.STATE_HIDDEN) {

            if (routeLaid) {
                view.setBottomSheetState(BottomState.Medium)
            } else {
                view.setBottomSheetState(BottomState.Small)
            }

            view.clearFocus()
            view.showSuggestionsRecyclerView(false)

        } else if (state == BottomSheetBehavior.STATE_EXPANDED) {

            if (keyboardHide) {

                if (routeLaid) {
                    view.setBottomSheetState(BottomState.Medium)
                } else {
                    view.setBottomSheetState(BottomState.Small)
                    view.showDropEditText(true)
                }

                view.clearFocus()
                view.showSuggestionsRecyclerView(false)

            } else {

                view.showSuggestionsRecyclerView(true)

            }

        } else if (state == BottomSheetBehavior.STATE_COLLAPSED) {

            view.getContext().hideKeyboard()
            view.showSuggestionsRecyclerView(false)
            view.showDropEditText(true)
            view.changeIconFirstField(false)
            view.showIconSecondField(false)
            view.clearFocus()

            keyboardHide = true

        }

    }

}