package com.egorka.delivery.modules.marketplaceActivity

import com.egorka.delivery.entities.Marketplaces
import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.handlers.NetworkHandler
import com.egorka.delivery.services.BottomState
import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect
import com.egorka.delivery.services.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MarketplacePresenter(private val view: MarketplaceActivityInterface): MarketplacePresenterInterface {

    private var mainService: MainService? = null
    private var bottomViewHide = true

    private var places: List<OrderLocation>? = null

    init { ServiceConnect(view.getContext()) { mainService = it ; onResume() } }

    override fun onResume() {

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
        view.getContext().hideKeyboard()
    }

    override fun pressPlaces() {
        places?.let { view.pressPlaces(it) }
    }

    override fun selectPlace(location: OrderLocation) {
        location.Point?.Name?.let { view.setPlaceLabel(it) }
    }

}