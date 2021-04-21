package com.egorka.delivery.modules.newOrderActivity

import android.os.Handler
import android.os.Looper
import android.view.View
import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.entities.*
import com.egorka.delivery.services.*
import com.google.android.material.bottomsheet.BottomSheetBehavior

class NewOrderPresenter(private val view: NewOrderActivityInterface): NewOrderPresenterInterface {

    private var mainService: MainService? = null
    private var keyboardHide = true

    override fun onStart() {

        mainService?.mainModel?.let { model ->

            updateLists(model)

            Handler(Looper.getMainLooper()).postDelayed({
                view.setBottomSheetState(BottomState.Small)
            }, 500)

        }

    }

    override fun onResume() {
        ServiceConnect(view.getContext()) { mainService = it ; onStart() }
    }

    private fun updateLists(model: MainModelInterface) {

        var index = 1

        model.pickups = model.pickups?.filter{ it.Point?.Code != null }?.toMutableList()
        model.drops = model.drops?.filter{ it.Point?.Code != null }?.toMutableList()

        model.pickups?.forEach {
            it.RouteOrder = index
            index++
        }

        model.drops?.forEach {
            it.RouteOrder = index
            index++
        }

        if ((model.pickups!!.size + model.drops!!.size) > 2) {
            view.updateAdapters(model.pickups!!, model.drops!!, NumState.Full)
        } else {
            view.updateAdapters(model.pickups!!, model.drops!!, NumState.Lite)
        }

    }

    override fun openDetails(location: NewOrderLocation, index: Int, view: View) {

        mainService?.mainModel?.let { model ->

            model.details = location
            model.detailsIndex = index

            GeneralRouter(this.view.getContext()).openDetailsActivity(view, this.view.getTransitionName())

        }

    }

    override fun newPickup() {

        mainService?.mainModel?.pickups?.last()?.RouteOrder?.let {

            val newPickup = NewOrderLocation(it + 1, LocationType.Pickup, Suggestion())

            mainService?.mainModel?.pickups?.add(newPickup)
            mainService?.mainModel?.details = newPickup
            mainService?.mainModel?.detailsIndex = mainService?.mainModel?.pickups?.size?.minus(1)

            GeneralRouter(view.getContext()).openNewLocationActivity()

        }

    }

    override fun newDrop() {

        mainService?.mainModel?.drops?.last()?.RouteOrder?.let {

            val newDrop = NewOrderLocation(it + 1, LocationType.Drop, Suggestion())

            mainService?.mainModel?.drops?.add(newDrop)
            mainService?.mainModel?.details = newDrop
            mainService?.mainModel?.detailsIndex = mainService?.mainModel?.drops?.size?.minus(1)

            GeneralRouter(view.getContext()).openNewLocationActivity()

        }

    }

    override fun removePickup(index: Int) {

        mainService?.mainModel?.let { model ->

            model.pickups?.removeAt(index)
            updateLists(model)

        }

    }

    override fun removeDrop(index: Int) {

        mainService?.mainModel?.let { model ->

            model.drops?.removeAt(index)
            updateLists(model)

        }

    }

    override fun openKeyboard() {
        keyboardHide = false
        view.hideBottomSheet()
    }

    override fun hideKeyboard() {

        keyboardHide = true
        view.getContext().hideKeyboard()

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBottomSheetState(BottomState.Small)
        }, 500)

    }

    override fun bottomStateChanged(state: Int) {
        if (keyboardHide) {
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