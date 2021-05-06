package com.egorka.delivery.modules.marketplaceActivity

import android.os.Handler
import android.os.Looper
import com.egorka.delivery.adapters.TypeData
import com.egorka.delivery.entities.*
import com.egorka.delivery.entities.Dictionary
import com.egorka.delivery.handlers.GoogleMapHandler
import com.egorka.delivery.handlers.LocationHandler
import com.egorka.delivery.handlers.NetworkHandler
import com.egorka.delivery.services.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*
import kotlin.collections.ArrayList

class MarketplacePresenter(private val view: MarketplaceActivityInterface): MarketplacePresenterInterface {

    private var mainService: MainService? = null
    private var locationHandler: LocationHandler? = null
    private var calendar = Calendar.getInstance().gmt3()
    private var bottomViewHide = true

    private var pickup: OrderLocation? = null
    private var drop = OrderLocation()
    private var places: List<OrderLocation>? = null

    init { ServiceConnect(view.getContext()) { mainService = it ; onResume() } }

    override fun onResume() {

        locationHandler = LocationHandler(view.getContext()) {
            pressMyLocation()
        }

        NetworkHandler(view.getContext()).getMarketPlaces {
            buildPlaces(it)
        }

    }

    private fun buildPlaces(locations: List<Marketplaces.MarketplacesPoint>) {

        places = ArrayList()

        locations.forEach {
            (places as ArrayList<OrderLocation>).add(OrderLocation(it, 1))
        }

    }

    override fun bottomStateChanged(state: Int) {
        if (!bottomViewHide) {
            when (state) {
                BottomSheetBehavior.STATE_HIDDEN -> {
                    view.setBottomSheetState(BottomState.Small)
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    view.setBottomSheetState(BottomState.Small)
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    view.setBottomSheetState(BottomState.Small)
                }
            }
        }
    }

    override fun hideKeyboard() {
        view.showSuggestions(false)
        view.getContext().hideKeyboard()
        calculateDelivery()
    }

    override fun pressMyLocation() {
        locationHandler?.location?.let {
            GoogleMapHandler(view.getContext()).getAddress(LatLng(it.latitude, it.longitude)) { address ->
                NetworkHandler(view.getContext()).searchAddress(address) { suggestions ->
                    val suggestion = suggestions[0]
                    pickup = OrderLocation(1, LocationType.Pickup, suggestion)
                    view.setPickupLabel(suggestion.Name!!)
                    calculateDelivery()
                }
            }
        }
    }

    override fun pressPlaces() {
        places?.let { view.pressPlaces(it) }
    }

    override fun selectPlace(location: OrderLocation) {
        drop = location
        location.Point?.Name?.let { view.setPlaceLabel(it) }
        calculateDelivery()
    }

    override fun selectTextField() {
        view.showSuggestions(true)
        view.hideBottomSheet()
    }

    override fun textDidChange(text: String) {
        NetworkHandler(view.getContext()).searchAddress(text) { suggestions ->
            view.setSuggestions(suggestions)
        }
    }

    override fun selectAddress(address: Dictionary.Suggestion) {

        view.setPickupLabel(address.Name.toString())
        view.setFocus(address.Name.toString().length)

        if (address.ID != null) {

            pickup = OrderLocation(1, LocationType.Pickup, address)
            view.deleteFocus()
            hideKeyboard()

        }

    }

    override fun pressDate() {
        view.showDatePicker(calendar)
    }

    override fun setDate(year: Int, month: Int, day: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        view.setDate(calendar.dayMonthYear())
    }

    private fun calculateDelivery() {

        if (pickup == null) { return }
        if (drop.Point == null) { return }

        view.hideBottomSheet()

        view.setPickupLabel(pickup!!.Point!!.Address!!)
        view.setPlaceLabel(drop.Point!!.Name!!)

        NetworkHandler(view.getContext()).calculateDelivery(DeliveryType.Track, listOf(pickup!!, drop)) {
            it.Result?.TotalPrice?.let { price -> updateBottomView(price) }
        }

    }

    private fun updateBottomView(price: Delivery.TotalPrice) {

        view.setInfoFields(TypeData(DeliveryType.Track), price)

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBottomSheetState(BottomState.Small)
        }, 300)

    }

    override fun openMarketMap() {

        mainService?.mainModel?.marketplaces = places
        GeneralRouter(view.getContext()).openMapActivity()

    }

    override fun boxCountChange(count: Int) {
        view.setBoxLabel(count.toString())
    }

    override fun palletCountChange(count: Int) {
        view.setPalletLabel(count.toString())
    }

}