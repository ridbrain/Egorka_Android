package com.egorka.delivery.modules.currentOrderActivity


import android.content.Context
import android.content.Intent
import com.egorka.delivery.services.BasePresenter
import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect

class CurrentOrderPresenter(override var view: CurrentOrderActivityInterface): BasePresenter, CurrentOrderPresenterInterface {

    override var mainService: MainService? = null
    private var serviceConnection = ServiceConnect(this)

    override fun onCreate() {
        view.getContext().bindService(Intent(view.getContext(), MainService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onBackPressed() {

    }

}