package com.egorka.delivery.modules.marketMap

import com.egorka.delivery.entities.OrderLocation
import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect

class MarketMapPresenter(override var view: MarketMapActivityInterface) : MarketMapPresenterInterface {

    private var mainService: MainService? = null

    init { ServiceConnect(view.getContext()) { mainService = it ; onResume() } }

    override fun onResume() {
        mainService?.mainModel?.marketplaces?.let {
            view.setPoints(it)
        }
    }

    override fun selectLocation(location: OrderLocation) {
        view.getContext().onBackPressed()
    }

}