package com.egorka.delivery.services

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

class ServiceConnect(val context: Activity, val callback: (MainService) -> Unit): ServiceConnection {

    init { context.bindService(Intent(context, MainService::class.java), this, Context.BIND_AUTO_CREATE) }

    override fun onServiceDisconnected(name: ComponentName?) { }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        callback((service as MainService.LocalBinder).getService())
    }

}