package com.egorka.delivery.modules.aboutActivity

import android.app.Activity

interface AboutActivityInterface {

    fun getContext(): Activity

}

interface AboutPresenterInterface {

    var view: AboutActivityInterface

    fun onStart()
    fun onResume()
    fun onPause()
    fun onBackPressed()

}