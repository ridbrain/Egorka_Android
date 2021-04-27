package com.egorka.delivery.modules.newOrderActivity

import android.os.Handler
import android.os.Looper
import android.view.View
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.adapters.TypeData
import com.egorka.delivery.entities.*
import com.egorka.delivery.handlers.NetworkHandler
import com.egorka.delivery.services.*
import com.google.android.material.bottomsheet.BottomSheetBehavior

class NewOrderPresenter(private val view: NewOrderActivityInterface): NewOrderPresenterInterface {

    private var mainService: MainService? = null
    private var bottomViewHide = true

    init { ServiceConnect(view.getContext()) { mainService = it ; onResume() } }

    override fun onResume() {

        updateLists()
        updateOrder()

    }

    private fun updateOrder() {

        mainService?.mainModel?.newOrder?.let { delivery ->

            delivery.Result?.Locations = delivery.Result?.Locations?.filter { it.Point?.Code != null }?.toMutableList()
            delivery.restoreIndex()

            NetworkHandler(view.getContext()).calculateDelivery(delivery.Type!!, delivery.Result?.Locations!!) {

                mainService?.mainModel?.newOrder = it
                updateLists()
                updateBottomView(it.Type!!, it.Result!!.TotalPrice!!)

            }

        }

    }

    private fun updateLists() {

        mainService?.mainModel?.newOrder?.let { delivery ->

            delivery.Result?.Locations = delivery.Result?.Locations?.filter { it.Point?.Code != null }?.toMutableList()

            delivery.Result?.Locations?.let { locations ->

                val pickups = locations.filter { it.Type == LocationType.Pickup }
                val drops = locations.filter { it.Type == LocationType.Drop }

                if ((pickups.size + drops.size) > 2) {
                    view.updateAdapters(pickups.toMutableList(), drops.toMutableList(), NumState.Full)
                } else {
                    view.updateAdapters(pickups.toMutableList(), drops.toMutableList(), NumState.Lite)
                }

            }

        }

    }

    private fun updateBottomView(type: DeliveryType, price: Delivery.TotalPrice) {

        view.setInfoFields(TypeData(type), price)

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBottomSheetState(BottomState.Small)
        }, 300)

    }

    override fun openDetails(location: OrderLocation, index: Int, view: View) {

        hideBottomView()

        Handler(Looper.getMainLooper()).postDelayed({

            mainService?.mainModel?.let { model ->

                model.details = location
                model.detailsIndex = index

                GeneralRouter(this.view.getContext()).openDetailsActivity(view, this.view.getTransitionName())

            }

        }, 300)

    }

    override fun newPickup() {

        hideBottomView()

        Handler(Looper.getMainLooper()).postDelayed({

            mainService?.mainModel?.newOrder?.Result?.Locations?.let { locations ->

                val pickups = locations
                    .filter { it.Type == LocationType.Pickup }
                    .sortedBy { it.RouteOrder }
                    .toMutableList()

                pickups.last().let { last ->

                    val new = OrderLocation(last.RouteOrder!! + 1, LocationType.Pickup, Dictionary.Suggestion())
                    pickups.add(new)

                    val drops = locations.filter { it.Type == LocationType.Drop }.toMutableList()
                    drops.forEach { it.RouteOrder = it.RouteOrder?.plus(1) }

                    mainService?.mainModel?.newOrder?.updateLocations(pickups, drops)
                    mainService?.mainModel?.detailsIndex = pickups.size - 1
                    mainService?.mainModel?.details = new

                    GeneralRouter(view.getContext()).openNewLocationActivity()

                }

            }

        }, 300)

    }

    override fun newDrop() {

        hideBottomView()

        Handler(Looper.getMainLooper()).postDelayed({

            mainService?.mainModel?.newOrder?.Result?.Locations?.let { locations ->

                val drops = locations
                    .filter { it.Type == LocationType.Drop }
                    .sortedBy { it.RouteOrder }
                    .toMutableList()

                drops.last().let { last ->

                    val new = OrderLocation(last.RouteOrder!! + 1, LocationType.Drop, Dictionary.Suggestion())
                    drops.add(new)

                    mainService?.mainModel?.newOrder?.updateLocations(locations.filter { it.Type == LocationType.Pickup }.toMutableList(), drops)
                    mainService?.mainModel?.detailsIndex = drops.size - 1
                    mainService?.mainModel?.details = new

                    GeneralRouter(view.getContext()).openNewLocationActivity()

                }

            }

        }, 300)

    }

    override fun deleteLocation(routeOrder: Int) {

        mainService?.mainModel?.newOrder?.let { delivery ->

            delivery.Result?.Locations = delivery.Result?.Locations?.filter{ it.RouteOrder != routeOrder }?.toMutableList()
            updateOrder()

        }

    }

    override fun hideBottomView() {
        bottomViewHide = true
        view.hideBottomSheet()
    }

    override fun hideKeyboard() {

        bottomViewHide = false
        view.getContext().hideKeyboard()

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBottomSheetState(BottomState.Small)
        }, 500)

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

}