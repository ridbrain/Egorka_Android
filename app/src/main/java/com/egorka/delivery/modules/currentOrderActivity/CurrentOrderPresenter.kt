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
//            if (model.newOrder == null) { return }
//
//            GoogleMapHandler(view.getContext()).getRoute(model.newOrder!!.Result!!.Locations!![0].Point!!, model.newOrder!!.Result!!.Locations!![1].Point!!) {
//                view.setPolyline(it)
//            }
//
//            model.newOrder?.Result?.Locations?.let { view.updateAdapter(it, NumState.Full) }
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