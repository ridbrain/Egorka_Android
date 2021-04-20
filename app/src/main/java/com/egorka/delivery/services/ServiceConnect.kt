package com.egorka.delivery.services

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class ServiceConnect(private val presenter: BasePresenter): ServiceConnection {

    override fun onServiceDisconnected(name: ComponentName?) {
        presenter.mainService = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        presenter.mainService = (service as MainService.LocalBinder).getService()
        presenter.onStart()
    }

}