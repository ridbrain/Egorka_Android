package com.egorka.delivery.modules.aboutActivity

import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect

class AboutPresenter(override var view: AboutActivityInterface): AboutPresenterInterface {

    private var mainService: MainService? = null

    init { ServiceConnect(view.getContext()) { mainService = it ; onStart() } }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onBackPressed() {

    }

}