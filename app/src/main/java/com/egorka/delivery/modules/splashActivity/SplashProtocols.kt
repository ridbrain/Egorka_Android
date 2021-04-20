package com.egorka.delivery.modules.splashActivity

import android.app.Activity
import android.view.View

interface SplashActivityInterface {
    fun getContext(): Activity
    fun getLogo(): View
    fun getTransitionName(): String
}

interface SplashPresenterInterface {
    fun onResume()
}