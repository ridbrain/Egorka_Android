package com.egorka.delivery.modules.currentOrderActivity

import android.app.Activity

interface CurrentOrderActivityInterface {

    fun getContext(): Activity

}

interface CurrentOrderPresenterInterface {

    var view: CurrentOrderActivityInterface

    fun onCreate()
    fun onResume()
    fun onPause()
    fun onBackPressed()

}