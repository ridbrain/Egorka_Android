package com.egorka.delivery.modules.errorActivity

import android.app.Activity

interface ErrorActivityInterface {

    fun getContext(): Activity

}

interface ErrorPresenterInterface {

    var view: ErrorActivityInterface

    fun onCreate()
    fun onResume()
    fun onPause()
    fun onBackPressed()

}