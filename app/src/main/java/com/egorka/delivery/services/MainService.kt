package com.egorka.delivery.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.egorka.delivery.entities.MainModel
import com.egorka.delivery.entities.MainModelInterface

class MainService : Service() {

    lateinit var mainModel: MainModelInterface

    override fun onCreate() {
        super.onCreate()
        mainModel = MainModel()
    }

    inner class LocalBinder : Binder() {
        fun getService(): MainService {
            return this@MainService
        }
    }

    private val mBinder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder { return  mBinder }

}