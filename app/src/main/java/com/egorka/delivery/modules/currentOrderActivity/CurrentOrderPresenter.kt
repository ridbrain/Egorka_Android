package com.egorka.delivery.modules.currentOrderActivity

import com.egorka.delivery.adapters.NumState
import com.egorka.delivery.handlers.GoogleMapHandler
import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect

class CurrentOrderPresenter(override var view: CurrentOrderActivityInterface): CurrentOrderPresenterInterface {

    private var mainService: MainService? = null

    init { ServiceConnect(view.getContext()) { mainService = it ; onStart() } }

    override fun onStart() {

//        mainService?.mainModel?.let { model ->
//
//            if (model.pickups == null) { return }
//            if (model.drops == null) { return }
//
//            val locations = mutableListOf(model.pickups!!.first(), model.drops!!.first())
//
//            GoogleMapHandler(view.getContext()).getRoute(locations.first().Point!!, locations.last().Point!!) {
//                view.setPolyline(it)
//            }
//
//            view.updateAdapter(locations, NumState.Full)
//
//        }

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onBackPressed() {

    }


}